export interface Game {
    id: string;
    mode: string;
    whitePlayer: string;
    blackPlayer: string;
    result: string;
    pgn: string
    date: Date;
}
