import {YOUTUBE_VIDEO_URL} from "../support/constants";

export class Video {
    id: string;
    title: string;
    linkImg: string;
    linkVideo: string;
    constructor(id: string, title: string, linkImg: string) {
        this.id = id;
        this.title = title;
        this.linkImg = linkImg;
        this.linkVideo=YOUTUBE_VIDEO_URL+id;
    }
}
