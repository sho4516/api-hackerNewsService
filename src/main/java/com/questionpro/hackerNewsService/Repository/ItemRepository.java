package com.questionpro.hackerNewsService.Repository;
import com.questionpro.hackerNewsService.Entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    Optional<Item> findByStoryId(int storyId);

    @Modifying
    @Query("UPDATE Item i SET i.by = :by, i.score = :score, i.time = :time, i.title = :title, i.type = :type, i.url = :url WHERE i.storyId = :storyId")
    void updateItem(@Param("storyId") int storyId, @Param("by") String by, @Param("score") int score, @Param("time") long time, @Param("title") String title, @Param("type") String type, @Param("url") String url);
}
