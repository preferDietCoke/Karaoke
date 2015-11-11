package com.preferdietcoke;

import com.preferdietcoke.model.SongBook;
import com.preferdietcoke.model.Song;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Queue;

public class KaraokeMachine {
	private SongBook songBook;
	private BufferedReader reader;
	private Map<String, String> menu;
	private Queue<Song> songQueue;

	public KaraokeMachine(SongBook songBook) {
		this.songBook = songBook;
		this.reader = new BufferedReader(new InputStreamReader(System.in));
		this.songQueue = new ArrayDeque<Song>();
		this.menu = new HashMap<String, String>();
		this.menu.put("add", "Add a new song to the song book");
		this.menu.put("play", "Play next song in queue");
		this.menu.put("choose", "Choose a song to sing!");
		this.menu.put("quit", "Give up. Exit the app");
	}

	private String promptAction() throws IOException {
		System.out.printf("There are %d songs available and %d in the queue. Your options are: %n",
			this.songBook.getSongCount(), this.songQueue.size());
		for (Map.Entry<String, String> option : this.menu.entrySet()) {
			System.out.printf("%s - %s %n",
				option.getKey(), option.getValue());
		}
		System.out.print("What do you want to do: ");
		String choice = this.reader.readLine();
		return choice.trim().toLowerCase();
	}

	private Song promptNewSong() throws IOException {
		System.out.print("Enter the artist's name: ");
		String artist = this.reader.readLine();
		System.out.print("Enter the title: ");
		String title = this.reader.readLine();
		System.out.print("Enter the video URL: ");
		String videoUrl = this.reader.readLine();
		return new Song(artist, title, videoUrl);
	}

	private int promptForIndex(List<String> options) throws IOException {
		int counter = 1;
		for (String option : options) {
			System.out.printf("%d.) %s %n", counter, option);
			counter++;
		}
		System.out.printf("Your choice:");
		String optionAsString = this.reader.readLine();
		int choice = Integer.parseInt(optionAsString);
		return choice-1;
	}

	private String promptArtist() throws IOException {
		System.out.println("Available artists:");
		List<String> artists = new ArrayList<>(this.songBook.getArtist());
		int index = promptForIndex(artists);
		return artists.get(index);
	}

	private Song promptSongForArtist(String artist) throws IOException {
		List<Song> songs = this.songBook.getSongsForArtist(artist);
		List<String> songTitles = new ArrayList<>();
		for (Song song : songs) {
			songTitles.add(song.getTitle());
		}
		System.out.printf("Available songs for %s: %n", artist);
		int index = promptForIndex(songTitles);
		return songs.get(index);
	}

	public void run() {
		String choice = "";
		loop: do {
			try {
				choice = promptAction();
				switch (choice) {
					case "add":
						Song song = this.promptNewSong();
						this.songBook.addSong(song);
						System.out.printf("%s added! %n%n", song);
						break;
					case "choose":
						String artist = promptArtist();
						Song artistSong = promptSongForArtist(artist);
						this.songQueue.add(artistSong);
						System.out.printf("You chose: %s %n", artistSong);
						break;
					case "play":
						this.playNext();
						break;
					case "quit":
						System.out.println("Thanks for playing!");
						break loop;
					default:
						System.out.printf("Unknown choice: '%s'. Try again. %n%n",
							choice);
						break;
				}
			} catch (IOException ioe) {
				System.out.println("Problem with input");
				ioe.printStackTrace();
			}
		} while (!choice.equals("quit"));
	}

	public void playNext() {
		Song song = this.songQueue.poll();
		if (song == null) {
			System.out.println("Sorry there are no songs in the queue. " +
			 									 "Use choose from the menu to add some");
		} else {
			System.out.printf("%n%nOpen %s to hear %s by %s %n%n",
				song.getVideoUrl(), song.getTitle(), song.getArtist());
		}
	}
}