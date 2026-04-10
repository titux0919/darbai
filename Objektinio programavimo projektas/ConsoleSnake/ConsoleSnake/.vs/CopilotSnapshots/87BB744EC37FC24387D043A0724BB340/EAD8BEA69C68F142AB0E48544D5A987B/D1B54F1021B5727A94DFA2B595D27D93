using ConsoleSnake.Domain;

namespace ConsoleSnake.Input
{
    // Modelis, apibūdinantis vieną įvesties momentą: gali būti kryptis arba komanda
    public readonly struct InputSnapshot
    {
        // Kryptis (pvz. Up/Down/Left/Right) arba null
        public Direction? Direction { get; init; }

        // Specifinė žaidimo komanda (pvz. Exit, Pause) arba null
        public GameCommand? Command { get; init; }
    }
}
