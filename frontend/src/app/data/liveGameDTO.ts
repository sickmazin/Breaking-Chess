export interface liveGameDTO {
  id: string;
  mode: string;
  whitePlayer: string;
  blackPlayer: string;
  turn: string;
  whiteTime: number;
  blackTime: number;
  drawRequest: boolean;
  fens: string[];
}
