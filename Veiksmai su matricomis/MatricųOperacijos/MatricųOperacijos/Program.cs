using System;
using System.Globalization;

// Aprašo vieną matricą ir veiksmus su ja
class Matrix
{
    public int Rows { get; }
    public int Cols { get; }
    private readonly double[,] data;

    public Matrix(int r, int c)
    {
        if (r <= 0 || c <= 0)
            throw new Exception("Eilučių ir stulpelių skaičius turi būti teigiamas.");

        Rows = r;
        Cols = c;
        data = new double[r, c];
    }

    // ----------- ĮVESTIS / IŠVESTIS -----------

    // Matricos įvedimas
    public void Read(string name)
    {
        Console.WriteLine($"\nĮveskite matricos {name} elementus ({Rows}x{Cols}).");
        Console.WriteLine("Pastaba: galima rašyti su '-' ir su kableliu arba tašku (pvz. -3,5 arba -3.5).");

        for (int i = 0; i < Rows; i++)
        {
            for (int j = 0; j < Cols; j++)
            {
                data[i, j] = ReadDouble($"[{i + 1},{j + 1}] = ");
            }
        }
    }

    // Matricos spausdinimas
    public void Print(string title)
    {
        Console.WriteLine($"\n{title} ({Rows}x{Cols}):");
        for (int i = 0; i < Rows; i++)
        {
            for (int j = 0; j < Cols; j++)
                Console.Write($"{data[i, j],10:F3} ");
            Console.WriteLine();
        }
    }

    // Leidžia įvesti skaičius su , arba .
    private static double ReadDouble(string prompt)
    {
        while (true)
        {
            Console.Write(prompt);
            string s = (Console.ReadLine() ?? "").Trim();

            // leidžiam ir , ir .
            s = s.Replace(',', '.');

            if (double.TryParse(s, NumberStyles.Float, CultureInfo.InvariantCulture, out double v))
                return v;

            Console.WriteLine("Bloga įvestis. Pvz: -3.5 arba -3,5");
        }
    }

    // ----------- VEIKSMAI SU MATRICOMIS -----------

    // Sudėtis A + B
    public static Matrix Add(Matrix a, Matrix b)
    {
        RequireSameSize(a, b, "sudėčiai");
        Matrix r = new Matrix(a.Rows, a.Cols);

        for (int i = 0; i < a.Rows; i++)
            for (int j = 0; j < a.Cols; j++)
                r.data[i, j] = a.data[i, j] + b.data[i, j];

        return r;
    }

    // Atimtis A - B
    public static Matrix Subtract(Matrix a, Matrix b)
    {
        RequireSameSize(a, b, "atimčiai");
        Matrix r = new Matrix(a.Rows, a.Cols);

        for (int i = 0; i < a.Rows; i++)
            for (int j = 0; j < a.Cols; j++)
                r.data[i, j] = a.data[i, j] - b.data[i, j];

        return r;
    }

    // Daugyba A * B
    public static Matrix Multiply(Matrix a, Matrix b)
    {
        if (a.Cols != b.Rows)
            throw new Exception("Daugybai reikia: A stulpeliai = B eilutės.");

        Matrix r = new Matrix(a.Rows, b.Cols);

        for (int i = 0; i < a.Rows; i++)
        {
            for (int k = 0; k < a.Cols; k++)
            {
                for (int j = 0; j < b.Cols; j++)
                {
                    r.data[i, j] += a.data[i, k] * b.data[k, j];
                }
            }
        }

        return r;
    }

    // Transponavimas A^T
    public Matrix Transpose()
    {
        Matrix t = new Matrix(Cols, Rows);
        for (int i = 0; i < Rows; i++)
            for (int j = 0; j < Cols; j++)
                t.data[j, i] = data[i, j];
        return t;
    }

    // Tikrina ar matricos vienodo dydžio (sudėčiai/atimčiai)
    private static void RequireSameSize(Matrix a, Matrix b, string opName)
    {
        if (a.Rows != b.Rows || a.Cols != b.Cols)
            throw new Exception($"Matricų dydžiai nesutampa ({opName} reikia vienodų dimensijų).");
    }
}

// ----------- PROGRAMOS VALDYMAS -----------
class Program
{
    // Saugiklis dėl atminties/našumo
    const int MAX_SIZE = 2000;

    // PATAISYTA: prompt spausdinamas tik jei jis perduotas
    static int ReadInt(string? prompt)
    {
        while (true)
        {
            if (!string.IsNullOrEmpty(prompt))
                Console.Write(prompt);

            string s = (Console.ReadLine() ?? "").Trim();
            if (int.TryParse(s, out int v)) return v;

            Console.WriteLine("Bloga įvestis. Įveskite sveiką skaičių.");
        }
    }

    static void PrintMenu()
    {
        Console.WriteLine("===== VEIKSMAI SU MATRICOMIS =====");
        Console.WriteLine("1) Sudėtis (A + B)");
        Console.WriteLine("2) Atimtis (A - B)");
        Console.WriteLine("3) Daugyba (A * B)");
        Console.WriteLine("4) Transponavimas (A^T)");
        Console.WriteLine("0) Baigti");
        Console.Write("Pasirinkimas: "); // PALIEKAM, kaip tu nori
    }

    static void Main()
    {
        while (true)
        {
            try
            {
                PrintMenu();

                // PATAISYTA: ReadInt NIEKO papildomai nespausdina,
                // nes "Pasirinkimas: " jau atspausdino PrintMenu()
                int choice = ReadInt(null);

                if (choice == 0)
                {
                    Console.WriteLine("Viso!");
                    return;
                }

                Console.WriteLine(); // kad po pasirinkimo viskas prasidėtų naujoje eilutėje

                switch (choice)
                {
                    case 1:
                    {
                        int r = ReadInt("Eilutės: ");
                        int c = ReadInt("Stulpeliai: ");
                        ValidateSize(r, c);

                        Matrix A = new Matrix(r, c);
                        Matrix B = new Matrix(r, c);

                        A.Read("A");
                        B.Read("B");

                        A.Print("A");
                        B.Print("B");

                        Matrix sum = Matrix.Add(A, B);
                        sum.Print("A + B");
                        break;
                    }

                    case 2:
                    {
                        int r = ReadInt("Eilutės: ");
                        int c = ReadInt("Stulpeliai: ");
                        ValidateSize(r, c);

                        Matrix A = new Matrix(r, c);
                        Matrix B = new Matrix(r, c);

                        A.Read("A");
                        B.Read("B");

                        A.Print("A");
                        B.Print("B");

                        Matrix res = Matrix.Subtract(A, B);
                        res.Print("A - B");
                        break;
                    }

                    case 3:
                    {
                        Console.WriteLine("Daugyba A*B: įveskite A dydį, tada B dydį.");

                        int rA = ReadInt("A eilutės: ");
                        int cA = ReadInt("A stulpeliai: ");
                        ValidateSize(rA, cA);

                        int rB = ReadInt("B eilutės: ");
                        int cB = ReadInt("B stulpeliai: ");
                        ValidateSize(rB, cB);

                        Matrix A = new Matrix(rA, cA);
                        Matrix B = new Matrix(rB, cB);

                        A.Read("A");
                        B.Read("B");

                        A.Print("A");
                        B.Print("B");

                        Matrix prod = Matrix.Multiply(A, B);
                        prod.Print("A * B");
                        break;
                    }

                    case 4:
                    {
                        int r = ReadInt("Eilutės: ");
                        int c = ReadInt("Stulpeliai: ");
                        ValidateSize(r, c);

                        Matrix A = new Matrix(r, c);
                        A.Read("A");
                        A.Print("A");

                        Matrix t = A.Transpose();
                        t.Print("A^T");
                        break;
                    }

                    default:
                        Console.WriteLine("Nėra tokio pasirinkimo.\n");
                        break;
                }

                Console.WriteLine("---------------------------------\n");
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Klaida: {ex.Message}\n");
            }
        }
    }

    static void ValidateSize(int r, int c)
    {
        if (r <= 0 || c <= 0)
            throw new Exception("Eilučių ir stulpelių skaičius turi būti teigiamas.");

        if (r > MAX_SIZE || c > MAX_SIZE)
            throw new Exception($"Leistinas dydis: 1..{MAX_SIZE} (dėl atminties/našumo).");
    }
}
