package com.example.nebojsa.newsforeveryone;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
final class QueryUtils {

    /** Sample JSON response for a USGS query */
    //private static final String SAMPLE_JSON_RESPONSE = "{\"status\":\"ok\",\"source\":\"the-next-web\",\"sortBy\":\"latest\",\"articles\":[{\"author\":\"TNW Deals\",\"title\":\"Choose from over 54,000 icons for your projects — and pay under $50\",\"description\":\"What does nearly every computer, device, operating system, program app or other digital creation have in common? They’re each represented by your own distinctive, highly identifiable ...\",\"url\":\"https://thenextweb.com/offers/2017/07/09/choose-54000-icons-projects-pay-50/\",\"urlToImage\":\"https://cdn3.tnwcdn.com/wp-content/blogs.dir/1/files/2017/07/b2K0Bno.jpg\",\"publishedAt\":\"2017-07-09T17:33:58Z\"},{\"author\":\"Ziv Elul\",\"title\":\"How publishers can fight Facebook and Google with audience data\",\"description\":\"Web publishers are falling on hard times. Houghton Mifflin Harcourt recently announced plans to lay off hundreds of employees. International Data Group laid off more than 90 people ...\",\"url\":\"https://thenextweb.com/business/2017/07/09/publishers-can-fight-facebook-google-audience-data/\",\"urlToImage\":\"https://cdn3.tnwcdn.com/wp-content/blogs.dir/1/files/2017/07/cms-265127_1280.jpg\",\"publishedAt\":\"2017-07-09T16:01:57Z\"},{\"author\":\"Yann Leretaille\",\"title\":\"Reining in the dastardly algorithms that are trying to control our lives\",\"description\":\"The prevailing view of artificial intelligence is that some day machines will help us reach better decisions than we can make on our own, improving our lives.\\r\\n\\r\\nThis view presumes ...\",\"url\":\"https://thenextweb.com/contributors/2017/07/09/reining-dastardly-algorithms-trying-control-lives/\",\"urlToImage\":\"https://cdn1.tnwcdn.com/wp-content/blogs.dir/1/files/2017/07/google-algorithm-fade-ss-1920-800x450.jpg\",\"publishedAt\":\"2017-07-09T12:01:33Z\"},{\"author\":\"Kris Duggan\",\"title\":\"6 lessons every business leader should learn from Uber’s uphill culture battle\",\"description\":\"Given the recent news headlines, it’s clear Uber — among other companies — must reevaluate its company culture. Earlier this week, when news hit that Uber’s board of directors ...\",\"url\":\"https://thenextweb.com/business/2017/07/09/6-lessons-every-business-leader-learn-ubers-uphill-culture-battle/\",\"urlToImage\":\"https://cdn0.tnwcdn.com/wp-content/blogs.dir/1/files/2017/07/Travis_Kalanick_-_Disrupt_SF_2012_7979998220.jpg\",\"publishedAt\":\"2017-07-09T00:01:09Z\"},{\"author\":\"David Geer\",\"title\":\"Earning  money in the mobile app era:how apps can help you to save more\",\"description\":\"Tech in the digital age is ubiquitous. Everywhere you look, you will find someone holding a smartphone, watching a movie on their tablets, or listening to music via their mobile devices.\\r\\n\\r\\nThe ...\",\"url\":\"https://thenextweb.com/contributors/2017/07/08/earning-money-mobile-app-erahow-apps-can-help-save/\",\"urlToImage\":\"https://cdn2.tnwcdn.com/wp-content/blogs.dir/1/files/2017/07/Mobile-app-one.jpg\",\"publishedAt\":\"2017-07-08T19:13:13Z\"},{\"author\":\"TNW Deals\",\"title\":\"Getflix and chill for life – it’s only $35 right now\",\"description\":\"You’re crashed on the couch, ready to relax and zone out on your favorite TV show or movie. It’s a moment of pure bliss. However, international geo-blocking means you probably can’t ...\",\"url\":\"https://thenextweb.com/offers/2017/07/08/getflix-chill-life-35-right-now/\",\"urlToImage\":\"https://cdn3.tnwcdn.com/wp-content/blogs.dir/1/files/2017/07/ZBmqZcp.jpg\",\"publishedAt\":\"2017-07-08T17:33:51Z\"},{\"author\":\"Aleks Kang\",\"title\":\"Will robots ever love us back?\",\"description\":\"“If someone here was a robot disguised as a human, who would it be?”\\r\\n\\r\\nMy coworkers and I play a game where we ask each other this question. Most go straight for the person who ...\",\"url\":\"https://thenextweb.com/contributors/2017/07/08/will-robots-ever-love-us-back/\",\"urlToImage\":\"https://cdn3.tnwcdn.com/wp-content/blogs.dir/1/files/2017/07/ava.jpg\",\"publishedAt\":\"2017-07-08T16:01:08Z\"},{\"author\":\"Alejandro Tauber\",\"title\":\"This battery-free cellphone sucks power out of thin air\",\"description\":\"Researchers from the University of Washington have developed the very first battery-free phone. Its sound quality is pretty shitty, but imagine not having to charge your phone because ...\",\"url\":\"https://thenextweb.com/gadgets/2017/07/08/battery-free-cellphone-sucks-power-thin-air/\",\"urlToImage\":\"https://cdn2.tnwcdn.com/wp-content/blogs.dir/1/files/2017/06/tnw-fb.png\",\"publishedAt\":\"2017-07-08T14:30:09Z\"},{\"author\":\"Maya Mikhailov\",\"title\":\"Retailers should embrace technology, but be careful of gimmicks\",\"description\":\"For today’s retailers, understanding shifting consumer trends can make the difference between a successful quarter or a painful restructuring. After being behind the curve in embracing ...\",\"url\":\"https://thenextweb.com/business/2017/07/08/retailers-embrace-technology-careful-gimmicks/\",\"urlToImage\":\"https://cdn3.tnwcdn.com/wp-content/blogs.dir/1/files/2017/07/VR_Retail-1.jpg\",\"publishedAt\":\"2017-07-08T12:01:30Z\"},{\"author\":\"Larry Alton\",\"title\":\"Advances in Pain Relief Technology Welcome to Millions of Chronic Sufferers\",\"description\":\"Chronic pain is something that millions of people deal with on a daily basis. And while the underlying cause isn’t always easily detectable or treatable, new technologies are providing ...\",\"url\":\"https://thenextweb.com/contributors/2017/07/08/advances-pain-relief-technology-welcome-millions-chronic-sufferers/\",\"urlToImage\":\"https://cdn3.tnwcdn.com/wp-content/blogs.dir/1/files/2016/12/MeizuBandModel.jpeg\",\"publishedAt\":\"2017-07-08T09:17:18Z\"}]}";

    private static final String LOG_TAG = QueryUtils.class.getName();
    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     +     * Query the USGS dataset and return a list of {@link News} objects.
     +     */
    static List<News> fetchNewsData(String requestUrl) {

        //test provera koja nit uspavljuje za 2 sekunde da bi proverili rad progres circle-a
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


                // Create URL object
                        URL url = createUrl(requestUrl);

                        // Perform HTTP request to the URL and receive a JSON response back
                                String jsonResponse = null;
                try {
                        jsonResponse = makeHttpRequest(url);
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Problem making the HTTP request.", e);
                    }

                        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s

        // Return the list of {@link Earthquake}s
                                return extractFeatureFromJson(jsonResponse);
            }


    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link News} objects that has been built up from
     * parsing a JSON response.
     */
    private static List<News> extractFeatureFromJson(String newsJSON) {
                // If the JSON string is empty or null, then return early.
                        if (TextUtils.isEmpty(newsJSON)) {
                        return null;
                    }

        List<News> news = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.

            // Create a JSONObject from the JSON response string
                        JSONObject baseJsonResponse = new JSONObject(newsJSON);
            JSONArray newsArray = baseJsonResponse.getJSONArray("articles");
            for(int i =0;i<newsArray.length(); i++){

                JSONObject currentNews = newsArray.getJSONObject(i);
                String author = currentNews.getString("author");
                String title = currentNews.getString("title");
                String description = currentNews.getString("description");
                String image = currentNews.getString("urlToImage");
                String time = currentNews.getString("publishedAt");
                String url = currentNews.getString("url");

                //slucaj kada u bazi nema podataka o autoru ili o vremenu
                if(currentNews.isNull("author")){
                    author = "Unknown";
                }
                if(currentNews.isNull("publishedAt")){
                    time = "Unknown";
                }
                if(currentNews.isNull("description")){
                    description = "Unknown";
                }
                if(currentNews.isNull("title")){
                    title = "Unknown";
                }
                if(currentNews.isNull("urlToImage")){
                    image = "http://www.hover.com/blog/wp-content/uploads/2015/08/404-page-error.png";
                }
                if(currentNews.getString("urlToImage").isEmpty()){
                    image = "http://www.hover.com/blog/wp-content/uploads/2015/08/404-page-error.png";
                }
                if(currentNews.isNull("url")){
                    url = "http://www.hover.com/blog/wp-content/uploads/2015/08/404-page-error.png";
                }
                if(currentNews.getString("url").isEmpty()){
                    url = "http://www.hover.com/blog/wp-content/uploads/2015/08/404-page-error.png";
                }

                News new_now = new News(author,title,time,image,description,url);
                news.add(new_now);

            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return news;
    }

}