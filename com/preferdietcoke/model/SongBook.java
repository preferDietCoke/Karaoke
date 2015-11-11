package com.preferdietcoke.model;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.io.*;
import java.util.Comparator;

public class SongBook {
	private List<Song> songs;

	public SongBook() {
		this.songs = new ArrayList<Song>();
	}

	public void exportTo(String fileName) {
		try (
			FileOutputStream fos = new FileOutputStream(fileName);
			PrintWriter writer = new PrintWriter(fos);
		) {
			for (Song song : this.songs) {
				writer.printf("%s|%s|%s%n",
					song.getArtist(),
					song.getTitle(),
					song.getVideoUrl());
			}
		} catch (IOException ioe) {
			System.out.printf("Problem saving %s%n", fileName);
			ioe.printStackTrace();
		}
	}

	public void importFrom(String fileName) {
		try (
			FileInputStream fis = new FileInputStream(fileName);
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
		) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] args = line.split("\\|");
				addSong(new Song(args[0], args[1], args[2]));
			}
		} catch (IOException ioe) {
			System.out.printf("Problems loading %s%n", fileName);
			ioe.printStackTrace();
		}
	}

	public void addSong(Song song) {
		this.songs.add(song);
	}

	public int getSongCount() {
		return this.songs.size();
	}
	// FIXME: THIS SHOULD BE HASHED
	private Map<String, List<Song>> byArtist() {
		Map<String, List<Song>> byArtist = new TreeMap<>();
		for (Song song : this.songs) {
			List<Song> artistSongs = byArtist.get(song.getArtist());
			if (artistSongs == null) {
				artistSongs = new ArrayList<>();
				byArtist.put(song.getArtist(), artistSongs);
			}
			artistSongs.add(song);
		}
		return byArtist;
	}

	public Set<String> getArtist() {
		return byArtist().keySet();
	}

	public List<Song> getSongsForArtist(String artistName) {
		List<Song> songs = byArtist().get(artistName);
		songs.sort(new Comparator<Song>() {
			@Override
			public int compare(Song song1, Song song2) {
				if (song1.equals(song2)) {
					return 0;
				}
				return song1.title.compareTo(song2.title);
			}
		});
		return songs;
	}
}