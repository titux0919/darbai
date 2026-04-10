using ConsoleSnake.Domain;
using System.Text;

namespace ConsoleSnake.Rendering
{
    // ConsoleRenderer atlieka rėminį atvaizdavimą į System.Console.
    // Jis naudoja dvimatį char bufferį, kuriame piešiama, o po to vienu metu pateikia tekstą į ekraną.
    public sealed class ConsoleRenderer : IRenderer
    {
        private int _width;
        private int _height;
        private char[,] _buffer = new char[1, 1];

        public void Initialize(int width, int height)
        {
            _width = width;
            _height = height;

            _buffer = new char[_height, _width];
            Console.CursorVisible = false;

            // Clear once to remove old content after a resize or first init
            try { Console.Clear(); } catch { }

            Clear();
        }

        public void Clear()
        {
            for (int y = 0; y < _height; y++)
            {
                for (int x = 0; x < _width; x++)
                {
                    _buffer[y, x] = ' ';
                }
            }
        }

        // Patikrina, ar pozicija yra ekrano ribose, tada piešia simbolį buferyje
        public void DrawCell(Point position, char ch)
        {
            if (position.X < 0 || position.X >= _width) return;
            if (position.Y < 0 || position.Y >= _height) return;
            _buffer[position.Y, position.X] = ch;
        }

        // Piešia tekstą horizontaliai nuo pozicijos
        public void DrawText(Point position, string text)
        {
            if (position.Y < 0 || position.Y >= _height) return;

            for (int i = 0; i < text.Length; i++)
            {
                int x = position.X + i;
                if (x < 0 || x >= _width) continue;
                _buffer[position.Y, x] = text[i];
            }
        }

        // Išveda bufferį į Console eilutė po eilutės, nustatant kursorių tik į eilutės pradžią.
        // Tai tiksliai perrašo kiekvieną konsolės eilutę, todėl nelieka paliktų simbolių.
        public void Present()
        {
            try
            {
                int consoleWidth = Console.WindowWidth;
                int consoleHeight = Console.WindowHeight;

                for (int y = 0; y < consoleHeight; y++)
                {
                    StringBuilder line = new StringBuilder(consoleWidth);

                    if (y < _height)
                    {
                        for (int x = 0; x < consoleWidth; x++)
                        {
                            if (x < _width)
                                line.Append(_buffer[y, x]);
                            else
                                line.Append(' ');
                        }
                    }
                    else
                    {
                        for (int x = 0; x < consoleWidth; x++) line.Append(' ');
                    }

                    Console.SetCursorPosition(0, y);
                    Console.Write(line.ToString());
                }
            }
            catch
            {
                // ignore console errors
            }
        }
    }
}
