namespace ConsoleSnake.Domain
{
    public sealed class GameSettings
    {
        public int Width { get; init; }
        public int Height { get; init; }

        public int StartDelayMs { get; init; }
        public int MinDelayMs { get; init; }
        public int SpeedStepMs { get; init; }

        public int PointsPerFood { get; init; }
        public int MilestoneStep { get; init; }

        public Difficulty Difficulty { get; init; }

        public static GameSettings ForDifficulty(Difficulty d) =>
            d switch
            {
                Difficulty.Easy => new GameSettings
                {
                    Width = 40,
                    Height = 20,
                    StartDelayMs = 200,
                    MinDelayMs = 60,
                    SpeedStepMs = 5,
                    PointsPerFood = 10,
                    MilestoneStep = 100,
                    Difficulty = d
                },

                Difficulty.Normal => new GameSettings
                {
                    Width = 60,
                    Height = 25,
                    StartDelayMs = 140,
                    MinDelayMs = 40,
                    SpeedStepMs = 4,
                    PointsPerFood = 10,
                    MilestoneStep = 150,
                    Difficulty = d
                },

                Difficulty.Hard => new GameSettings
                {
                    Width = 80,
                    Height = 30,
                    StartDelayMs = 90,
                    MinDelayMs = 30,
                    SpeedStepMs = 3,
                    PointsPerFood = 10,
                    MilestoneStep = 200,
                    Difficulty = d
                },

                _ => throw new ArgumentOutOfRangeException(nameof(d))
            };
    }
}
