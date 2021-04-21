package com.infrrd.training.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infrrd.training.entity.User;

@Component
public class UserRepositoryImpl implements UserRepository {
	
	@Autowired
	private ObjectMapper objectMapper;

	RestHighLevelClient client = new RestHighLevelClient(
			RestClient.builder(new HttpHost("localhost",9200,"http")));
	
	@Override
	public List<User> findAllUserDetailsFromElastic() {
		// connecting to elastic search
		SearchRequest searchRequest = new SearchRequest("userimage");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		searchRequest.source(searchSourceBuilder);
		
		List<User> userList = new ArrayList<>();
		SearchResponse searchResponse = null;
		
		try {
			searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
			if(searchResponse.getHits().getTotalHits().value>0) {
				SearchHit[] searchHit = searchResponse.getHits().getHits();
				for(SearchHit hit:searchHit) {
					Map<String, Object> map = hit.getSourceAsMap();
					userList.add(objectMapper.convertValue(map, User.class));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return userList;
	}

	@Override
	public List<User> findUserByUserName(String userName) {
		// SearchRequest object is created to send write request to search and retrieve documents from index
		SearchRequest searchRequest = new SearchRequest("userimage");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("userName.keyword", userName)));
		searchRequest.source(searchSourceBuilder);
		
		// User documents that are returned are stored in a list
		List<User> userList = new ArrayList<>();
		SearchResponse searchResponse = null;
		
		try {
			// Response is stored in a SearchResponse Object
			searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
			
			if(searchResponse.getHits().getTotalHits().value>0) {
				SearchHit[] searchHit = searchResponse.getHits().getHits();
				for(SearchHit hit:searchHit) {
					Map<String, Object> map = hit.getSourceAsMap();
					// ObjectMapper from Jackson is used to map SearchHit objects to User class
					userList.add(objectMapper.convertValue(map, User.class));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return userList;
	}

	@Override
	public List<User> findUserByUserNameAndAddress(String userName, String address) {
		SearchRequest searchRequest = new SearchRequest("userimage");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		
		// termQuery() is used to specify the term that is to be searched with.
		// userName.keyword tells that the words must be in the same sequence as that in userName and avoid individual token matches 
		searchSourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("userName.keyword", userName))
															.must(QueryBuilders.matchQuery("address",address)));
		searchRequest.source(searchSourceBuilder);
		List<User> userList = new ArrayList<>();
		SearchResponse searchResponse = null;
		try {
			searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
			if(searchResponse.getHits().getTotalHits().value>0) {
				SearchHit[] searchHit = searchResponse.getHits().getHits();
				for(SearchHit hit:searchHit) {
					Map<String, Object> map = hit.getSourceAsMap();
					userList.add(objectMapper.convertValue(map, User.class));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return userList;
	}

	@Override
	public void addNewUser(String uid, String userName, String address) {
		// IndexRequest is used to create request for inserting a new document in the already existing index
		IndexRequest indexRequest = new IndexRequest("userimage");
		String jsonString = "{" +
							"\"userId\":\""+uid+"\""+","+
							"\"userName\":\""+userName+"\""+","+
							"\"address\":\""+address+"\""+
							"}";
		indexRequest.source(jsonString, XContentType.JSON);
		try {
			client.index(indexRequest,RequestOptions.DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void deleteUser(String uid) {
		// Search for the document in the index to retrieve it's _id
		SearchRequest searchRequest = new SearchRequest("userimage");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("userId", uid)));
		searchRequest.source(searchSourceBuilder);
		String documentId="";
		SearchResponse searchResponse = null;
		try {
			searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
			if(searchResponse.getHits().getTotalHits().value>0) {
				SearchHit[] searchHit = searchResponse.getHits().getHits();
				for(SearchHit hit:searchHit) {
					documentId = hit.getId();
				}
			}
			
			// DeleteRequest is used to create the request for deleting a document from the index
			DeleteRequest deleteRequest = new DeleteRequest("userimage",documentId);
			DeleteResponse deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
			if(deleteResponse.getResult() == DocWriteResponse.Result.NOT_FOUND) {
				System.out.println("Document not found");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

