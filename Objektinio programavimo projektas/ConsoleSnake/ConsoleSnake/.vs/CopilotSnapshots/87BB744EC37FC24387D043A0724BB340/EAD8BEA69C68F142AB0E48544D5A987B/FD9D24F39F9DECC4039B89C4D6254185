namespace ConsoleSnake.Input
{
    // Sąsaja, kuri apibrėžia įvesties šaltinį.
    // Implementacijos gali būti klaviatūra, testų fake arba kitas įvesties įrenginys.
    public interface IInputSource
    {
        // Bando nuskaityti vieną įvesties įvykį (komandą arba kryptį).
        // Grąžina true jei buvo įvykis ir užpildo snapshot.
        bool TryRead(out InputSnapshot snapshot);
    }
}
