export interface liveGameDTO {
  id: string;
  type: string;
  whitePlayer: string;
  blackPlayer: string;
  turn: string;
  result: string;
  whiteTime: number;
  blackTime: number;
  drawRequest: boolean;
  fens: string[];
}
