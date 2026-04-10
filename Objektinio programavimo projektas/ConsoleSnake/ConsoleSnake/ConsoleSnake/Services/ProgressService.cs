using ConsoleSnake.Domain;

namespace ConsoleSnake.Services
{
    /// <summary>
    /// Atsakingas už Score, HiScore, greitėjimą, milestone ir NEW RECORD pranešimus.
    /// SRP: vien tik progresas/taškai/rekordai/banneriai.
    /// </summary>
    public sealed class ProgressService : IProgressService
    {
        private readonly IHighScoreStore _store;

        // Dabartinis žaidimo score ir aukščiausias rezultatas (hi-score)
        public int Score { get; private set; }
        public int HiScore { get; private set; }

        // Greitis (ms per tick) — naudojamas Thread.Sleep
        public int TickDelay { get; private set; }

        // Milestone banner (pvz. kas 500 taškų)
        private int _nextMilestone;
        private string? _milestoneText;
        private DateTime _milestoneUntilUtc;

        // New record banner laikas
        private DateTime _newRecordUntilUtc;

        // Konstruktoriumi gaunam IHighScoreStore, iš kurio užkraunam pradinį hi-score
        public ProgressService(IHighScoreStore store)
        {
            _store = store;
            HiScore = _store.Load();
        }

        // Reset nustato pradinius laukus pagal GameSettings (pvz. po restart)
        public void Reset(GameSettings settings)
        {
            Score = 0;
            TickDelay = settings.StartDelayMs;

            _nextMilestone = settings.MilestoneStep;
            _milestoneText = null;
            _milestoneUntilUtc = DateTime.MinValue;
            _newRecordUntilUtc = DateTime.MinValue;
        }

        /// <summary>
        /// Kviesti tik tada, kai suvalgytas maistas.
        /// Padidina score, pritaiko greitėjimą, tikrina milestone ir hi-score.
        /// </summary>
        public void OnFoodEaten(GameSettings settings)
        {
            // Padidinam score
            Score += settings.PointsPerFood;

            // Greitėjimas: mažinam TickDelay, bet ne žemiau MinDelayMs
            if (TickDelay > settings.MinDelayMs)
            {
                TickDelay -= settings.SpeedStepMs;
                if (TickDelay < settings.MinDelayMs)
                    TickDelay = settings.MinDelayMs;
            }

            // Milestone: jeigu pasiekta _nextMilestone, paruošiam bannerį
            if (Score >= _nextMilestone)
            {
                _milestoneText = $"MILESTONE! {_nextMilestone} pts!";
                _milestoneUntilUtc = DateTime.UtcNow.AddSeconds(2.5);
                _nextMilestone += settings.MilestoneStep;
            }

            // Hi-score: visada atnaujinam ir išsaugom jei įveiktas
            if (Score > HiScore)
            {
                HiScore = Score;
                _store.Save(HiScore);

                // NEW RECORD banner rodomas tik jei taškai yra tikslinis milestone (pvz. kas 500)
                if (settings.MilestoneStep > 0 && (Score % settings.MilestoneStep) == 0)
                {
                    _newRecordUntilUtc = DateTime.UtcNow.AddSeconds(2.0);
                }
            }
        }

        // Grąžina milestone banner tekstą jei dar galioja laike
        public string? GetMilestoneBannerText()
            => (_milestoneText is not null && DateTime.UtcNow <= _milestoneUntilUtc)
                ? _milestoneText
                : null;

        // Ar rodyti mirksintį NEW RECORD pranešimą (naudoja Environment.TickCount)?
        public bool ShouldShowNewRecordBlink()
        {
            if (DateTime.UtcNow > _newRecordUntilUtc) return false;
            return ((Environment.TickCount / 200) % 2) == 0; // mirksi kas ~200ms
        }

        // Išvalo hi-score failą ir nustato HiScore į 0
        public void ClearHighScores()
        {
            _store.Clear();
            HiScore = 0;
        }

        // Grąžina top istoriją iš saugyklos (naudojama GAME OVER ekrane)
        public int[] GetTopScores()
        {
            var hist = _store.LoadHistory();
            return hist.Length == 0 ? Array.Empty<int>() : hist;
        }
    }
}
