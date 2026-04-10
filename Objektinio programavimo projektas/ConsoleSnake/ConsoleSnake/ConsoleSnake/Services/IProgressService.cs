using ConsoleSnake.Domain;

namespace ConsoleSnake.Services
{
    public interface IProgressService
    {
        int Score { get; }
        int HiScore { get; }
        int TickDelay { get; }

        void Reset(GameSettings settings);
        void OnFoodEaten(GameSettings settings);

        string? GetMilestoneBannerText();
        bool ShouldShowNewRecordBlink();

        // new helpers for hi-score history and controls
        void ClearHighScores();
        int[] GetTopScores();
    }
}
