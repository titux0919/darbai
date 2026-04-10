namespace ConsoleSnake.Domain
{
    // Paprastas Point modelis X/Y koordinatėms
    public readonly record struct Point(int X, int Y)
    {
        public Point Move(Point direction) => new(X + direction.X, Y + direction.Y);
    }
}
