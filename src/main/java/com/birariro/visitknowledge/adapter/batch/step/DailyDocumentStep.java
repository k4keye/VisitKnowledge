package com.birariro.visitknowledge.adapter.batch.step;

import com.birariro.visitknowledge.adapter.batch.step.event.BatchActionEvent;
import com.birariro.visitknowledge.adapter.batch.step.event.DailyDocumentEvent;
import com.birariro.visitknowledge.adapter.message.event.Events;
import com.birariro.visitknowledge.adapter.persistence.jpa.library.Document;
import com.birariro.visitknowledge.adapter.persistence.jpa.library.Library;
import com.birariro.visitknowledge.adapter.persistence.jpa.library.LibraryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 하루에 한번
 * 사용자들 에게 보낼 document 들을 찾는 배치
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class DailyDocumentStep {
    private final StepBuilderFactory stepBuilderFactory;
    private final LibraryRepository libraryRepository;
    private final CustomStepExecutionListener customStepExecutionListener;


    @Bean
    public Step dailyDocumentExtractStep(){
        return stepBuilderFactory.get("dailyDocumentExtractStep")
                .listener(customStepExecutionListener)
                .<Document, Document> chunk(100)
                .reader(dailyDocumentExtractReader())
                .processor(dailyDocumentExtractProcessor())
                .writer(dailyDocumentExtractWriter())
                .build();
    }


    @Bean
    @StepScope
    public ListItemReader<Document> dailyDocumentExtractReader(){
        List<Library> libraries = libraryRepository.findActiveByAll();
        List<Document> collect = libraries.stream()
                .flatMap(library -> library.getWaitDocuments().stream())
                .collect(Collectors.toList());

        if(collect.size() > 0) Events.raise(new DailyDocumentEvent(collect));
        else Events.raise(new BatchActionEvent(false,"오늘은 볼것이 없습니다."));

        return new ListItemReader<>(collect);
    }


    public ItemProcessor<Document, Document> dailyDocumentExtractProcessor(){
        return item ->{
            log.info("wait doc  >>>> "+item.getTitle());
            return item;
        };
    }

    public ItemWriter<Document> dailyDocumentExtractWriter(){
        return items -> {
            log.info("[dailyDocumentExtractWriter] items count : "+items.size());
            for (Document item : items) {
                item.sendComplete();
            }
        };
    }

}
