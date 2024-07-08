export interface liveGame {
  id: string;
  //modality: string;
  whitePlayer: string;
  blackPlayer: string;
  validMove: boolean;
  whiteTime: number;
  blackTime: number;
  drawRequest: boolean;
  fen: string[];
}
