package project.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.backend.data.Book;
import project.backend.data.Player;
import project.backend.repository.LikeRepository;

import java.util.List;

@Service
public class LikeService {
    private final LikeRepository likeRepository;

    @Autowired
    public LikeService (LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    @Transactional(readOnly = true)
    public List<Book> getLikes(Player player) {
        return this.likeRepository.findAllBookLikedByPlayer(player);
    }

}
