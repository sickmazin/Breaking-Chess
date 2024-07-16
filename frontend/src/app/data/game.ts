import {Player} from "./player";

export interface Game {
    id:string;
    type: string;
    whitePlayer: Player;
    blackPlayer: Player;
    result: string;
    date: Date;
}