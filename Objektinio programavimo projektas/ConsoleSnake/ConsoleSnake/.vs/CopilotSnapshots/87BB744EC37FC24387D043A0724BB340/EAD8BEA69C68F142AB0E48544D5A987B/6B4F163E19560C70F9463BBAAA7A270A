using ConsoleSnake.Domain;

namespace ConsoleSnake.Input
{
    // KeyboardInputSource skaito Console.ReadKey ir paverčia į InputSnapshot'us.
    // Intercept=true reiškia, kad klavišo paspaudimas nebus parodytas konsolėje.
    public sealed class KeyboardInputSource : IInputSource
    {
        public bool TryRead(out InputSnapshot snapshot)
        {
            snapshot = default;

            if (!Console.KeyAvailable)
                return false;

            var key = Console.ReadKey(intercept: true).Key;

            // komandos
            if (key == ConsoleKey.Escape)
            {
                snapshot = new InputSnapshot { Command = GameCommand.Exit };
                return true;
            }

            if (key == ConsoleKey.P)
            {
                snapshot = new InputSnapshot { Command = GameCommand.TogglePause };
                return true;
            }

            if (key == ConsoleKey.R)
            {
                snapshot = new InputSnapshot { Command = GameCommand.Restart };
                return true;
            }

            if (key == ConsoleKey.D1 || key == ConsoleKey.NumPad1)
            {
                snapshot = new InputSnapshot { Command = GameCommand.DifficultyEasy };
                return true;
            }

            if (key == ConsoleKey.D2 || key == ConsoleKey.NumPad2)
            {
                snapshot = new InputSnapshot { Command = GameCommand.DifficultyNormal };
                return true;
            }

            if (key == ConsoleKey.D3 || key == ConsoleKey.NumPad3)
            {
                snapshot = new InputSnapshot { Command = GameCommand.DifficultyHard };
                return true;
            }

            // H - pagalbos perjungimas
            if (key == ConsoleKey.H)
            {
                snapshot = new InputSnapshot { Command = GameCommand.ToggleHelp };
                return true;
            }

            // C - išvalyti hi-score istoriją
            if (key == ConsoleKey.C)
            {
                snapshot = new InputSnapshot { Command = GameCommand.ClearHiScores };
                return true;
            }

            // kryptys
            var dir = key switch
            {
                ConsoleKey.UpArrow or ConsoleKey.W => Direction.Up,
                ConsoleKey.DownArrow or ConsoleKey.S => Direction.Down,
                ConsoleKey.LeftArrow or ConsoleKey.A => Direction.Left,
                ConsoleKey.RightArrow or ConsoleKey.D => Direction.Right,
                _ => (Direction?)null
            };

            if (dir is null) return false;

            snapshot = new InputSnapshot { Direction = dir };
            return true;
        }
    }
}
