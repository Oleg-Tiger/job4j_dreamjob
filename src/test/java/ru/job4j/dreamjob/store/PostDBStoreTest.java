package ru.job4j.dreamjob.store;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;

import java.util.List;

public class PostDBStoreTest {

    @Test
    public void whenCreatePost() {
        PostDBStore store = new PostDBStore(new Main().loadPool());
        Post post = new Post(0, "Java Job");
        City city = new City(1, "Москва");
        post.setCity(city);
        store.add(post);
        Post postInDb = store.findById(post.getId());
        Assertions.assertThat(postInDb.getName()).isEqualTo(post.getName());
    }

    @Test
    public void whenUpdatePost() {
        PostDBStore store = new PostDBStore(new Main().loadPool());
        Post post = new Post(0, "Java");
        City city = new City(1, "Москва");
        post.setCity(city);
        store.add(post);
        post.setName("Java Job");
        store.update(post);
        Post postInDb = store.findById(post.getId());
        Assertions.assertThat(postInDb.getName()).isEqualTo(post.getName());
    }

    @Test
    public void whenFindAllPosts() {
        PostDBStore store = new PostDBStore(new Main().loadPool());
        Post post = new Post(0, "Java");
        Post post2 = new Post(1, "Java 2");
        Post post3 = new Post(2, "Java 3");
        City city = new City(1, "Москва");
        post.setCity(city);
        post2.setCity(city);
        post3.setCity(city);
        List<Post> resultBefore = store.findAll();
        store.add(post);
        store.add(post2);
        store.add(post3);
        List<Post> result = store.findAll();
        Assertions.assertThat(resultBefore.size() + 3).isEqualTo(result.size());
        Assertions.assertThat(result).contains(post, post2, post3);
    }
}