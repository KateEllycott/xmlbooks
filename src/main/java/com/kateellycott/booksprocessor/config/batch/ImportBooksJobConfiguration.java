package com.kateellycott.booksprocessor.config.batch;

import com.kateellycott.booksprocessor.model.Book;
import com.kateellycott.booksprocessor.model.BookSection;
import com.kateellycott.booksprocessor.repository.BookSectionRepository;
import com.kateellycott.booksprocessor.services.BookService;
import java.util.Arrays;
import java.util.List;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;


@Configuration
public class ImportBooksJobConfiguration {

  @Autowired
  private BookService bookService;
  @Autowired
  private BookSectionRepository bookSectionRepository;
  @Autowired
  private JobBuilderFactory jobBuilderFactory;
  @Autowired
  private StepBuilderFactory stepBuilderFactory;

  @Bean
  public Job job() {
    return this.jobBuilderFactory.get("importBooksJob")
        .start(importBooksStep())
        .validator(validator())
        .incrementer(new RunIdIncrementer())
        .build();
  }

  @Bean
  public Step importBooksStep() {
    return this.stepBuilderFactory.get("importBooksStep")
        .<Book, List<BookSection>>chunk(1)
        .reader(bookInfoReader(null))
        .processor(compositeItemProcessor())
        .writer(itemWriter())
        .build();
  }

  @Bean
  public JobParametersValidator validator() {
    DefaultJobParametersValidator validator = new DefaultJobParametersValidator();
    validator.setRequiredKeys(new String[] {"inputFile", "importFolder"});
    validator.setOptionalKeys(new String[] {"run.id"});
    return validator;
  }

  @Bean
  @StepScope
  public FlatFileItemReader<Book> bookInfoReader(@Value("#{jobParameters['inputFile']}")String inputFile) {
    return new FlatFileItemReaderBuilder<Book>()
        .name("bookLinkReader")
        .resource(new ClassPathResource(inputFile))
        .delimited()
        .names(new String[]{"name", "link"})
        .fieldSetMapper(new BeanWrapperFieldSetMapper<Book>() {
            { setTargetType(Book.class);
          }})
        .build();
  }

  @Bean
  public CompositeItemProcessor<Book, List<BookSection>> compositeItemProcessor() {
    CompositeItemProcessor<Book, List<BookSection>> itemProcessor = new CompositeItemProcessor<>();
    itemProcessor.setDelegates(Arrays.asList(bookInfoValidatingItemProcessor(), bookSectionsProcessor(null)));
    return itemProcessor;
  }

  @Bean
  public BeanValidatingItemProcessor<Book> bookInfoValidatingItemProcessor() {
    return new BeanValidatingItemProcessor<>();
  }

  @Bean
  @StepScope
  public ItemProcessor<Book, List<BookSection>> bookSectionsProcessor(
      @Value("#{jobParameters['importFolder']}")String importFolder) {
    return new BookExportProcessor().setImportFolder(importFolder);
  }

  @Bean
  public ItemWriter<List<BookSection>> itemWriter() {
    return items -> {
      items.forEach(item -> bookSectionRepository.saveAll(item));
    };
  }
}
