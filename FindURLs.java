import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public class FindURLs 
{
	public static void main(String[] args)
	{
		String source;
		try
		{
			source = args[0];
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			System.out.println("Please enter the URL!");
			return;
		}
		
		// get page source from given URL
		FindURLs find =  new FindURLs();
		LinkedList<String> page = find.getPage(source);
		if (page == null)
		{
			return;
		}
		
		// add all found URLs and secure URLs into linked lists
		LinkedList<String> URLs = find.getURLs(page);
		LinkedList<String> secureURLs = find.getSecureURLs(URLs);
		
		// print out the number and the whole list of (secure) URLs
		System.out.println("Total URLs: " + URLs.size());
		for (String line : URLs) 
		{
		    System.out.println("> " + line);
		}
		System.out.println("Total secure URLs: " + secureURLs.size());
		for (String line : secureURLs) 
		{
		    System.out.println("Secure link: " + line);
		}
	}
		
	// get page source as a linked list, line by line
	public LinkedList<String> getPage(String source)
	{
		URL url;
		try 
		{
			url = new URL(source);
		} 
		catch (MalformedURLException e) 
		{
			System.out.println("Please enter the valid URL!");
			return null;
		}
		// get input stream
		BufferedReader in;
		try 
		{
			in = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
		} 
		catch (IOException e) 
		{
			System.out.println("Could not get an input stream.");
			return null;
		}
		// read input stream and add each line into linked list
		LinkedList<String> lines = new LinkedList();
		String readLine;
		try 
		{
			while ((readLine = in.readLine()) != null) 
			{
			    lines.add(readLine);
			}
		} 
		catch (IOException e) 
		{
			System.out.println("Could not read.");
			return null;
		}
		return lines;
	}

	// get all the URLs from page source as a linked list
	public LinkedList<String> getURLs(LinkedList<String> page)
	{
		LinkedList<String> URLs = new LinkedList();
        	for (String line : page)
        	{
        		int start = 0;
        		int index = line.indexOf("href=", start);
                if (index != -1)
                {
                	int firstQuote = index + 6;
                    int lastQuote = line.indexOf("\"", firstQuote);
                    String sub = line.substring(firstQuote, lastQuote);
                    if(sub.startsWith("http"))
                    {
                        URLs.add(sub);
                    }
                }
        	}
		return URLs;
	}

	// create a new linked list to get only secure (https) URLs
	public LinkedList<String> getSecureURLs(LinkedList<String> URLs)
	{
		LinkedList<String> secureURLs = new LinkedList();
		for (String line : URLs)
		{
			if(line.startsWith("https"))
			{
				secureURLs.add(line);
			}
		}
		return secureURLs;
	}
}
