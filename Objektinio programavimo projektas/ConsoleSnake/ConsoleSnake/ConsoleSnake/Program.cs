using ConsoleSnake.Core;
using ConsoleSnake.Input;
using ConsoleSnake.Rendering;
using ConsoleSnake.Services;

namespace ConsoleSnake
{
    internal static class Program
    {
        static void Main()
        {
            while (true)
            {
                var menu = new MainMenu();
                var choice = menu.Show();

                if (choice == MenuResult.Exit)
                    return;

                bool restart;

                do
                {
                    IRenderer renderer = new ConsoleRenderer();
                    IInputSource input = new KeyboardInputSource();
                    IHighScoreStore store = new FileHighScoreStore("hiscore.txt");
                    IProgressService progress = new ProgressService(store);

                    var game = new Game(renderer, input, progress);
                    var result = game.Run();

                    restart = result == GameResult.Restart;

                } while (restart);
            }
        }
    }
}
