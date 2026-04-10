namespace ConsoleSnake.Domain
{
    // Kryptys, kurias gali turėti gyvatė
    public enum Direction
    {
        Up,
        Down,
        Left,
        Right
    }

    public static class DirectionExtensions
    {
        public static Point ToPoint(this Direction direction) =>
            direction switch
            {
                Direction.Up => new Point(0, -1),
                Direction.Down => new Point(0, 1),
                Direction.Left => new Point(-1, 0),
                Direction.Right => new Point(1, 0),
                _ => new Point(0, 0)
            };
    }
}
