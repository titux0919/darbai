namespace ConsoleSnake.Services
{
    // IHighScoreStore abstrahuoja hi-score saugojimą. Implementacija gali rašyti į failą arba kitur.
    public interface IHighScoreStore
    {
        // Senas API: užkrauna aukščiausią rezultatą (jei saugomas tik single int)
        int Load();
        void Save(int hiScore);

        // Nauji metodai: istorija ir išvalymas
        int[] LoadHistory(); // užkrauna visą istoriją (pvz. paskutinius 5 rezultatus)
        void SaveHistory(int[] scores); // išsaugo pateiktą istoriją
        void Clear(); // išvalo visus įrašus
    }
}
