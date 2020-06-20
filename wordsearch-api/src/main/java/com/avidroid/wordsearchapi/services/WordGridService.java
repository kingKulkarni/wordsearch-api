package com.avidroid.wordsearchapi.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

@Service
public class WordGridService {

	private enum Direction{
		HORIZONTAL,
		VERTICAL,
		DIAGONAL,
		HORIZONTAL_INVERSE,
		VERTICAL_INVERSE,
		DIAGONAL_INVERSE
	}
		
		private class Coordinates{
			int x;
			int y;
			
			Coordinates(int x, int y){
				this.x = x;
				this.y = y;
			}
		}
		
		// Fill Grid
		public char[][] generateGrid(int gridSize, List<String> words) {
			
			List<Coordinates> coordinates = new ArrayList();
			char[][] contents = new char[gridSize][gridSize];
			
			for(int i=0; i<gridSize; i++) {
				for(int j=0; j<gridSize; j++) {
					
					coordinates.add(new Coordinates(i, j));
					contents[i][j] = '_';
				}
			}
			
			for(String word : words) {
				Collections.shuffle(coordinates);	
				for(Coordinates coordinate : coordinates) {
					int x = coordinate.x;
					int y = coordinate.y;
					
					Direction selectedDirection = getDirection(contents, word, coordinate);
					
					if(selectedDirection != null) {
						
						switch(selectedDirection) {
							
							case HORIZONTAL:
								for(char c : word.toCharArray()){
									contents[x][y++] = c;	
								}
								break;
							case VERTICAL:	
								for(char c : word.toCharArray()){
									contents[x++][y] = c;	
								}
								break;
							case DIAGONAL:
								for(char c : word.toCharArray()){
									contents[x++][y++] = c;	
								}
								break;
								
							case HORIZONTAL_INVERSE:
								for(char c : word.toCharArray()){
									contents[x][y--] = c;	
								}
								break;
							case VERTICAL_INVERSE:	
								for(char c : word.toCharArray()){
									contents[x--][y] = c;	
								}
								break;
							case DIAGONAL_INVERSE:
								for(char c : word.toCharArray()){
									contents[x--][y--] = c;	
								}
								break;	
						}
						break;
					}
				}	
			}
			randomFillGrid(contents);
			
			return contents;
		}
		
		// Display Grid
		public void displyGrid(char[][] contents){
			
			int gridSize = contents[0].length;
			
			for(int i=0;i<gridSize;i++) {
				for(int j=0;j<gridSize;j++) {
					System.out.print(contents[i][j]+" ");
				}
				System.out.println();
			}		
			
		}
		
		// Random Fill Grid
		public void randomFillGrid(char[][] contents){
			
			int gridSize = contents[0].length;
			
			String allCapLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			
			for(int i=0;i<gridSize;i++) {
				for(int j=0;j<gridSize;j++) {
					if(contents[i][j] == '_') {
						int randomIndex = ThreadLocalRandom.current().nextInt(0, allCapLetters.length());			
						contents[i][j] = allCapLetters.charAt(randomIndex);
					}
				}
			}	
		}
		
		// Get Direction
		private Direction getDirection(char[][] contents,String word, Coordinates coordinate){
			
			List<Direction> directions = Arrays.asList(Direction.values());
			Collections.shuffle(directions);
			
			for(Direction direction : directions){
				if(doesFit(contents,word, coordinate, direction))
					return direction;
			}
			return null;
		}
		
		private boolean doesFit(char[][] contents,String word, Coordinates coordinate, Direction direction) {
			
			int wordLength = word.length();
			int gridSize = contents[0].length;
			
			switch(direction) {
				case HORIZONTAL:
					if(coordinate.y + wordLength > gridSize) return false;
					for(int i=0; i<wordLength;i++) {
						char leter = contents[coordinate.x][coordinate.y + i];
						if(leter != '_' && leter != word.charAt(i)) return false;
					}
					break;
					
				case VERTICAL:
					if(coordinate.x + wordLength > gridSize) return false;
					for(int i=0; i<wordLength;i++) {
						char leter = contents[coordinate.x +i][coordinate.y];
						if(leter != '_' && leter != word.charAt(i)) return false;
					}
					break;
					
				case DIAGONAL:
					if(coordinate.x + wordLength > gridSize || coordinate.y + wordLength > gridSize) return false;
					for(int i=0; i<wordLength;i++) {
						char leter = contents[coordinate.x +i][coordinate.y+i];
						if(leter != '_' && leter != word.charAt(i)) return false;
					}
					break;
				
				case HORIZONTAL_INVERSE:
					if(coordinate.y < wordLength) return false;
					for(int i=0; i<wordLength;i++) {
						char leter = (contents[coordinate.x][coordinate.y - i]);
						if(leter != '_' && leter != word.charAt(i)) return false;
					}
					break;
					
				case VERTICAL_INVERSE:
					if(coordinate.x < wordLength) return false;
					for(int i=0; i<wordLength;i++) {
						char leter = (contents[coordinate.x-1][coordinate.y]);
						if(leter != '_' && leter != word.charAt(i)) return false;
					}
					break;
					
				case DIAGONAL_INVERSE:
					if(coordinate.x < wordLength || coordinate.y < wordLength) return false;
					for(int i=0; i<wordLength;i++) {
						char leter = (contents[coordinate.x-1][coordinate.y - i]);
						if(leter != '_' && leter != word.charAt(i)) return false;
					}
					break;	
			}
			return true;
		}
	}