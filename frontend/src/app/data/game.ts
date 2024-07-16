import {Player} from "./player";

export interface Game {
    id: string;
    mode: string;
    whitePlayer: Player;
    blackPlayer: Player;
    result: string;
    pgn: string
    date: Date;
}
