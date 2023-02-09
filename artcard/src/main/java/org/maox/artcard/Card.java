package org.maox.artcard;

/**
 * Card
 * 
 * @author Alex
 *
 */
public class Card {
	private int index = -1;
	private String cardName;
	private String faction;
	private String artist;
	private String url;
	private String urlImage;

	/**
	 * @return the cardName
	 */
	public String getCardName() {
		return cardName;
	}

	/**
	 * @param cardName the cardName to set
	 */
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	/**
	 * @return the faction
	 */
	public String getFaction() {
		return faction;
	}

	/**
	 * @param faction the faction to set
	 */
	public void setFaction(String faction) {
		this.faction = faction;
	}

	/**
	 * @return the artist
	 */
	public String getArtist() {
		return artist;
	}

	/**
	 * @param artist the artist to set
	 */
	public void setArtist(String artist) {
		this.artist = artist;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @return the urlImage
	 */
	public String getUrlImage() {
		return urlImage;
	}

	/**
	 * @param urlImage the urlImage to set
	 */
	public void setUrlImage(String urlImage) {
		this.urlImage = urlImage;
	}

	/**
	 * getFileName
	 * 
	 * @return
	 */
	public String getFileName() {
		StringBuilder fileName = new StringBuilder();
		if (index != -1)
			fileName.append(String.format("%04d", index) + " - ");
		
		fileName.append(cardName.replaceAll("[-+.^:,]", ""));

		if (faction != null)
			fileName.append(" ("+faction+")");

		if (artist != null)
			fileName.append(" ("+artist.replaceAll("[-+.^:,]", "")+")");
		
		fileName.append(".jpg");
		return fileName.toString();
	}
}
