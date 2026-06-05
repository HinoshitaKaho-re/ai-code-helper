package com.example.aicodehelper.ai.rag;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 加载RAG
 */
@Configuration
public class RagConfig {

    @Resource
    private EmbeddingModel qwenEmbeddingModel;

    @Resource
    EmbeddingStore<TextSegment> embeddingStore;

     //RAG组件的创建方法
     @Bean
     public ContentRetriever contentRetriever() {
         //创建一个基于向量数据库的内容检索器，注入嵌入模型
         //1、加载文档
         List<Document> documents = FileSystemDocumentLoader.loadDocuments("src/main/resources/docs");

         //2、切割文档，每个文档按照段落进行切割，最大1000个字符，最多重叠200个字符
         DocumentByParagraphSplitter documentByParagraphSplitter =
                 new DocumentByParagraphSplitter(1000, 200);

         //3、自定义文档加载器，把文档转换成向量并保存到向量数据库
         EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                 .documentSplitter(documentByParagraphSplitter)
                 // 为了提高搜索质量，为每个 TextSegment 添加文档名称
                 .textSegmentTransformer(textSegment -> TextSegment.from(textSegment.metadata().getString("file_name") + "\n" + textSegment.text(), textSegment.metadata()))
                 // 使用指定的向量模型
                 .embeddingModel(qwenEmbeddingModel)
                 // 指定向量存储
                 .embeddingStore(embeddingStore)
                 .build();

        //加载文档
        ingestor.ingest(documents);

        //4、自定义内容查询器
         ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                 .embeddingStore(embeddingStore)
                 .embeddingModel(qwenEmbeddingModel)
                 .maxResults(5) //最多 5 个检索结果
                 .minScore(0.75) // 过滤掉分数小于0.75的结果
                 .build();

         return contentRetriever;
     }
}
