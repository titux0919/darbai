namespace ConsoleSnake.Core
{
    public class MainMenu
    {
        public MenuResult Show()
        {
            Console.Clear();
            Console.CursorVisible = false;

            int w = Console.WindowWidth;
            int h = Console.WindowHeight;

            string title = "=== CONSOLE SNAKE ===";
            string start = "1 - Start";
            string exit = "2 - Exit";

            int centerX(string text) => Math.Max(0, (w - text.Length) / 2);

            int startY = h / 2 - 2;

            Console.SetCursorPosition(centerX(title), startY);
            Console.WriteLine(title);

            Console.SetCursorPosition(centerX(start), startY + 2);
            Console.WriteLine(start);

            Console.SetCursorPosition(centerX(exit), startY + 3);
            Console.WriteLine(exit);

            // NĖRA "Pasirinkimas:" – laukiam tik klavišo
            while (true)
            {
                var key = Console.ReadKey(true).Key;

                if (key == ConsoleKey.D1 || key == ConsoleKey.NumPad1)
                    return MenuResult.Start;

                if (key == ConsoleKey.D2 || key == ConsoleKey.NumPad2)
                    return MenuResult.Exit;
            }
        }
    }

    public enum MenuResult
    {
        Start,
        Exit
    }
}
