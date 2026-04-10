using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading;

using ConsoleSnake.Domain;
using ConsoleSnake.Input;
using ConsoleSnake.Rendering;
using ConsoleSnake.Services;

namespace ConsoleSnake.Core
{
    public class Game
    {
        private readonly IRenderer _renderer;
        private readonly IInputSource _input;
        private readonly IProgressService _progress;

        private GameSettings _settings;

        private bool _isRunning = true;
        private bool _isPaused = false;
        private bool _gameOver = false;
        private bool _showHelp = false;

        private Snake _snake = null!;
        private Point _food;

        public Game(IRenderer renderer, IInputSource input, IProgressService progress)
        {
            _renderer = renderer;
            _input = input;
            _progress = progress;

            _settings = GameSettings.ForDifficulty(Difficulty.Normal);
            ResetGame();
        }

        public GameResult Run()
        {
            _renderer.Initialize(_settings.Width, _settings.Height);

            while (_isRunning)
            {
                HandleInput();

                if (!_isPaused && !_gameOver)
                    Update();

                Render();
                Thread.Sleep(_progress.TickDelay);
            }

            return WaitForGameOverDecision();
        }

        private void HandleInput()
        {
            while (_input.TryRead(out var snapshot))
            {
                if (snapshot.Command is not null)
                {
                    switch (snapshot.Command.Value)
                    {
                        case GameCommand.Exit:
                            _isRunning = false;
                            _gameOver = true;
                            return;

                        case GameCommand.TogglePause:
                            if (!_gameOver) _isPaused = !_isPaused;
                            break;

                        case GameCommand.Restart:
                            ResetGame();
                            break;

                        case GameCommand.DifficultyEasy:
                            ApplyDifficulty(Difficulty.Easy);
                            break;

                        case GameCommand.DifficultyNormal:
                            ApplyDifficulty(Difficulty.Normal);
                            break;

                        case GameCommand.DifficultyHard:
                            ApplyDifficulty(Difficulty.Hard);
                            break;

                        case GameCommand.ToggleHelp:
                            _showHelp = !_showHelp;
                            break;

                        case GameCommand.ClearHiScores:
                            _progress.ClearHighScores();
                            break;
                    }
                }

                if (snapshot.Direction is not null && !_isPaused && !_gameOver)
                    _snake.SetDirection(snapshot.Direction.Value);
            }
        }

        private void ApplyDifficulty(Difficulty d)
        {
            _settings = GameSettings.ForDifficulty(d);
            _renderer.Initialize(_settings.Width, _settings.Height);
            ResetGame();
        }

        private void Update()
        {
            var nextHead = _snake.GetNextHeadPosition();

            if (nextHead.X <= 0 || nextHead.X >= _settings.Width - 1 ||
                nextHead.Y <= 0 || nextHead.Y >= _settings.Height - 1)
            {
                _gameOver = true;
                return;
            }

            if (_snake.IsCollidingWithSelf(nextHead))
            {
                _gameOver = true;
                return;
            }

            bool grow = nextHead == _food;
            _snake.Move(grow);

            if (grow)
            {
                _progress.OnFoodEaten(_settings);
                _food = SpawnFood();
            }
        }

        private void Render()
        {
            _renderer.Clear();

            int w = _settings.Width;
            int h = _settings.Height;

            // Frame
            for (int x = 0; x < w; x++)
            {
                _renderer.DrawCell(new Point(x, 0), '#');
                _renderer.DrawCell(new Point(x, h - 1), '#');
            }

            for (int y = 0; y < h; y++)
            {
                _renderer.DrawCell(new Point(0, y), '#');
                _renderer.DrawCell(new Point(w - 1, y), '#');
            }

            // Snake
            bool first = true;
            foreach (var p in _snake.Body)
            {
                _renderer.DrawCell(p, first ? '@' : 'o');
                first = false;
            }

            // Food (VISADA po HUD zonos)
            if (_food.X > 0 && _food.Y > 0)
                _renderer.DrawCell(_food, 'X');

            // HUD
            _renderer.DrawText(
                new Point(2, h - 1),
                $"Score:{_progress.Score}  Hi:{_progress.HiScore}  Diff:{_settings.Difficulty}  Spd:{_progress.TickDelay}ms"
            );

            _renderer.DrawText(
                new Point(2, 1),
                "P=Pause  R=Restart  ESC=Exit  1/2/3=Diff  H=Help  C=ClearHi"
            );

            // MILESTONE
            var milestone = _progress.GetMilestoneBannerText();
            if (milestone is not null)
            {
                _renderer.DrawText(
                    new Point(Math.Max(2, w / 2 - milestone.Length / 2), 2),
                    milestone
                );
            }

            if (_isPaused)
                _renderer.DrawText(new Point(w / 2 - 5, h / 2), "== PAUSED ==");

            if (_showHelp)
            {
                string[] lines =
                {
                    "Controls:",
                    "Arrow/WASD - move",
                    "P - pause",
                    "R - restart",
                    "1/2/3 - difficulty",
                    "H - help",
                    "C - clear hi-scores",
                    "ESC - exit"
                };

                for (int i = 0; i < lines.Length; i++)
                    _renderer.DrawText(new Point(2, 3 + i), lines[i]);
            }

            if (_gameOver)
            {
                _renderer.DrawText(new Point(w / 2 - 6, h / 2 - 1), "== GAME OVER ==");
                _renderer.DrawText(new Point(w / 2 - 9, h / 2), "Press R to restart");
                _renderer.DrawText(new Point(w / 2 - 8, h / 2 + 1), "Press ESC to exit");

                var top = _progress.GetTopScores();
                for (int i = 0; i < top.Length; i++)
                {
                    string line = $"{i + 1}. {top[i]}";
                    _renderer.DrawText(new Point(w / 2 - line.Length / 2, h / 2 + 3 + i), line);
                }
            }

            _renderer.Present();
        }

        private GameResult WaitForGameOverDecision()
        {
            while (true)
            {
                if (Console.KeyAvailable)
                {
                    var key = Console.ReadKey(true).Key;

                    if (key == ConsoleKey.R)
                        return GameResult.Restart;

                    if (key == ConsoleKey.Escape)
                        return GameResult.ExitToMenu;
                }

                Thread.Sleep(50);
            }
        }

        private void ResetGame()
        {
            _isPaused = false;
            _gameOver = false;

            _progress.Reset(_settings);

            _snake = new Snake(new Point(_settings.Width / 2, _settings.Height / 2));
            _food = SpawnFood();
        }

        private Point SpawnFood()
        {
            var freeCells = new List<Point>();

            // y = 0  -> rėmas
            // y = 1  -> HUD
            // y = 2  -> milestone
            // y >= 3 -> žaidimo laukas
            for (int x = 1; x < _settings.Width - 1; x++)
            {
                for (int y = 3; y < _settings.Height - 2; y++)
                {
                    var p = new Point(x, y);
                    if (!_snake.Body.Contains(p))
                        freeCells.Add(p);
                }
            }

            if (freeCells.Count == 0)
            {
                _gameOver = true;
                return new Point(-1, -1);
            }

            return freeCells[Random.Shared.Next(freeCells.Count)];
        }
    }
}
