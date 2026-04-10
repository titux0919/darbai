using System.Collections.Generic;

namespace ConsoleSnake.Domain
{
    // `Snake` klasė reprezentuoja žaidimo gyvatę: turi kūno sąrašą (Body), kryptį ir metodus judėjimui.
    public class Snake
    {
        public LinkedList<Point> Body { get; } = new LinkedList<Point>();
        private Direction _direction = Direction.Right;

        public Snake(Point start)
        {
            // Pridedam pradžios galvutę
            Body.AddFirst(start);
        }

        // Grąžina kitą galvos poziciją pagal dabartinę kryptį
        public Point GetNextHeadPosition()
        {
            var head = Body.First.Value;
            return _direction switch
            {
                Direction.Up => new Point(head.X, head.Y - 1),
                Direction.Down => new Point(head.X, head.Y + 1),
                Direction.Left => new Point(head.X - 1, head.Y),
                Direction.Right => new Point(head.X + 1, head.Y),
                _ => head
            };
        }

        // Nustatyti kryptį (negalima daryti 180 laipsnių posūkio iš karto)
        public void SetDirection(Direction d)
        {
            // Neleidžiam priešingos krypties tiesioginio pakeitimo
            if ((_direction == Direction.Left && d == Direction.Right) ||
                (_direction == Direction.Right && d == Direction.Left) ||
                (_direction == Direction.Up && d == Direction.Down) ||
                (_direction == Direction.Down && d == Direction.Up))
                return;

            _direction = d;
        }

        // Judėjimo metodas: jei grow==true, prideda galvą ir nepašalina uodegos (gyvatė ilgėja)
        public void Move(bool grow)
        {
            var next = GetNextHeadPosition();
            Body.AddFirst(next);
            if (!grow)
            {
                Body.RemoveLast();
            }
        }

        // Patikrina ar nauja galvos pozicija susiduria su bet kuriuo kūno segmentu
        public bool IsCollidingWithSelf(Point p)
        {
            foreach (var b in Body)
            {
                if (b == p) return true;
            }
            return false;
        }
    }
}
