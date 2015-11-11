package com.preferdietcoke.model;

public class Song {
	protected String artist;
	protected String title;
	protected String videoUrl;

	public Song(String artist, String title, String videoUrl) {
		this.artist = artist;
		this.title = title;
		this.videoUrl = videoUrl;
	}

	public String getArtist() {
		return this.artist;
	}

	public String getTitle() {
		return this.title;
	}

	public String getVideoUrl() {
		return this.videoUrl;
	}

	@Override
	public String toString() {
		return String.format("Song: %s by %s", this.title, this.artist);
	}
}