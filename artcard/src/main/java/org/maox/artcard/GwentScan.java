package org.maox.artcard;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Scan Gwent Cards
 * 
 * @author Alex
 *
 */
public class GwentScan {

	private static final Logger logger = LoggerFactory.getLogger(GwentScan.class);

	private static final String URL_STARTER_SET = "https://gwent.pro/list/?search=Starter%20Set";
	private static final String URL_BASE_SET = "https://gwent.pro/list/?search=Base%20Set";
	
	private static final String PATH_STARTER_SET = "img/gwent/starter set/";
	private static final String PATH_BASE_SET = "img/gwent/base set/";

	/**
	 * Constructor
	 * 
	 * @param expansion
	 */
	public GwentScan() {
	}

	/**
	 * Launcher
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			GwentScan scan = new GwentScan();
			scan.start();

		} catch (Exception e) {
			handleException(e);
		}
	}

	/**
	 * start
	 * 
	 * @throws Exception
	 */
	private void start() throws Exception {
		saveImages(PATH_STARTER_SET, readPage(URL_STARTER_SET));
		saveImages(PATH_BASE_SET, readPage(URL_BASE_SET));
	}

	/**
	 * Write Images from a List of Cards
	 * 
	 * @param readPage
	 */
	private void saveImages(String path, List<Card> listCard) throws Exception {
		
		logger.info("Start saving images");
		
		// Check dir
		File dir = new File(path);
		if (!dir.exists())
		{
			logger.warn("Creating dir {}", path);
			dir.mkdir();
		}
		
		// Extract images
		for (Card card : listCard)
		{
			String destinationFile = path+card.getFileName();
			URL url = new URL(card.getUrlImage());
		    InputStream is = url.openStream();
		    OutputStream os = new FileOutputStream(destinationFile);

		    byte[] b = new byte[2048];
		    int length;

		    while ((length = is.read(b)) != -1) {
		        os.write(b, 0, length);
		    }

		    is.close();
		    os.close();
		}

		logger.info("End saving images");
	}

	/*
	 * Read webpage from gwent.pro and extract image URLs
	 */
	private List<Card> readPage(String URL) throws Exception {
		logger.info("Start reading page: {}", URL);

		// Get page
		Document doc = Jsoup.connect(URL).get();
		logger.info("Read page: {}", doc.title());

		// Extract info <table>
		Elements tables = doc.body().getElementsByTag("table");
		// Cards are in second table <tbody><tr>
		Elements cards = tables.get(1).child(0).children();
		cards.remove(0); // Remove the header

		List<Card> listCards = new ArrayList<Card>();

		// From TableRow extract Card info
		for (Element card : cards) {
			listCards.add(extractCard(card));
		}

		logger.info("End reading page. Load {} Cards", listCards.size());

		return listCards;
	}

	/**
	 * extractCard
	 * 
	 * @param card
	 * @return
	 */
	private Card extractCard(Element cardHTML) {

		Card card = new Card();

		Elements rowData = cardHTML.children();
		card.setCardName(rowData.get(2).child(0).child(0).child(0).text()); // Card Name
		card.setUrl(rowData.get(2).child(0).child(0).child(0).attr("href")); // Link
		card.setFaction(rowData.get(3).child(0).text()); // Faction
		card.setArtist(rowData.get(4).child(0).text()); // Artist
		card.setUrlImage(rowData.get(7).child(0).attr("href")); // Image Link

		// Card ID extract from the url
		Pattern pattern = Pattern.compile("(\\d+)");
		Matcher matcher = pattern.matcher(card.getUrl());
		if (matcher.find())
			card.setIndex(Integer.parseInt(matcher.group()));

		return card;
	}

	/**
	 * Handle Exception
	 *
	 * @param e exception
	 */
	public static void handleException(Exception e) {
		logger.error(e.toString());

		StackTraceElement[] arrayOfStackTraceElement = e.getStackTrace();
		for (StackTraceElement element : arrayOfStackTraceElement) {
			logger.error("\tat {}", element);
		}
	}

}
