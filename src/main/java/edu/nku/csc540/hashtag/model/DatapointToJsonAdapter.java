package edu.nku.csc540.hashtag.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
	"timestamp",
	"hashtagCounts"
})

public class DatapointToJsonAdapter {

	@JsonProperty("timestamp")
	private Long timestamp;
	@JsonProperty("hashtagCounts")
	private List<HashtagCount> hashtagCounts = new ArrayList<HashtagCount>();
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * No args constructor for use in serialization
	 * 
	 */
	public DatapointToJsonAdapter() {
	}

	/**
	 * 
	 * @param timestamp
	 * @param hashtagCounts
	 */
	public DatapointToJsonAdapter(Long timestamp, List<HashtagCount> hashtagCounts) {
		this.timestamp = timestamp;
		this.hashtagCounts = hashtagCounts;
	}

	public DatapointToJsonAdapter(LocalDateTime ts, Map<String, Integer> hashtagCountsMap) {
		ZoneId zoneId = ZoneId.systemDefault();
		this.timestamp = ts.atZone(zoneId).toEpochSecond();
		List<HashtagCount> hashtagCountLst = new ArrayList<HashtagCount>();
		hashtagCountsMap.entrySet().forEach(e -> {
			hashtagCountLst.add(new HashtagCount(e.getKey(),e.getValue().longValue()));
		});
		this.hashtagCounts = hashtagCountLst;
	}

	/**
	 * 
	 * @return
	 * The timestamp
	 */
	@JsonProperty("timestamp")
	public Long getTimestamp() {
		return timestamp;
	}

	/**
	 * 
	 * @param timestamp
	 * The timestamp
	 */
	@JsonProperty("timestamp")
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * 
	 * @return
	 * The hashtagCounts
	 */
	@JsonProperty("hashtagCounts")
	public List<HashtagCount> getHashtagCounts() {
		return hashtagCounts;
	}

	/**
	 * 
	 * @param hashtagCounts
	 * The hashtagCounts
	 */
	@JsonProperty("hashtagCounts")
	public void setHashtagCounts(List<HashtagCount> hashtagCounts) {
		this.hashtagCounts = hashtagCounts;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	public class HashtagCount {

		@JsonProperty("hashtag")
		private String hashtag;
		@JsonProperty("count")
		private Long count;
		@JsonIgnore
		private Map<String, Object> additionalProperties = new HashMap<String, Object>();

		/**
		 * No args constructor for use in serialization
		 * 
		 */
		public HashtagCount() {
		}

		/**
		 * 
		 * @param count
		 * @param hashtag
		 */
		public HashtagCount(String hashtag, Long count) {
			this.hashtag = hashtag;
			this.count = count;
		}

		/**
		 * 
		 * @return
		 * The hashtag
		 */
		@JsonProperty("hashtag")
		public String getHashtag() {
			return hashtag;
		}

		/**
		 * 
		 * @param hashtag
		 * The hashtag
		 */
		@JsonProperty("hashtag")
		public void setHashtag(String hashtag) {
			this.hashtag = hashtag;
		}

		/**
		 * 
		 * @return
		 * The count
		 */
		@JsonProperty("count")
		public Long getCount() {
			return count;
		}

		/**
		 * 
		 * @param count
		 * The count
		 */
		@JsonProperty("count")
		public void setCount(Long count) {
			this.count = count;
		}

		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this);
		}

		@JsonAnyGetter
		public Map<String, Object> getAdditionalProperties() {
			return this.additionalProperties;
		}

		@JsonAnySetter
		public void setAdditionalProperty(String name, Object value) {
			this.additionalProperties.put(name, value);
		}
	}
}

