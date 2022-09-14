package ru.job4j.dreamjob.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.store.PostDBStore;
import java.util.Collection;
import java.util.List;

@ThreadSafe
@Service
public class PostService {

    private final PostDBStore store;
    private final CityService cityService = new CityService();

    public PostService(PostDBStore store) {
        this.store = store;
    }

    public Post add(Post post) {
        return store.add(post);
    }

    public Post findById(int id) {
        Post result = store.findById(id);
        result.setCity(cityService.findById(result.getCity().getId()));
        return result;
    }

    public boolean update(Post post) {
        return store.update(post);
    }

    public Collection<Post> findAll() {
        List<Post> posts = store.findAll();
        posts.forEach(
                post -> post.setCity(
                        cityService.findById(post.getCity().getId())
                )
        );
        return posts;
    }
}
