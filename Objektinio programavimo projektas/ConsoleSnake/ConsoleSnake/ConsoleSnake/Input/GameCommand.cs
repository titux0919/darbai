namespace ConsoleSnake.Input
{
    // GameCommand apibrėžia visas žaidimo komandas, kurias gali siųsti InputSnapshot
    public enum GameCommand
    {
        Exit,
        TogglePause,
        Restart,
        DifficultyEasy,
        DifficultyNormal,
        DifficultyHard,
        ToggleHelp,
        ClearHiScores
    }
}
