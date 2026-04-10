using ConsoleSnake.Domain;

namespace ConsoleSnake.Rendering
{
    // IRenderer abstrahuoja visą atvaizdavimo logiką. Dėl to Game nepriklauso nuo System.Console tiesiogiai.
    public interface IRenderer
    {
        void Initialize(int width, int height); // paruošia bufferį / terminalo langą
        void Clear(); // išvalo ekrano bufferį
        void DrawCell(Point position, char ch); // nupaišo vieną simbolį pozicijoje
        void DrawText(Point position, string text); // nupaišo tekstą
        void Present(); // atspausdina bufferį į ekraną
    }
}
