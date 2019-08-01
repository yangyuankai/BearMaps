package bearmaps.proj2c;

import bearmaps.hw4.streetmap.Node;
import bearmaps.hw4.streetmap.StreetMapGraph;
import bearmaps.proj2ab.Point;
import bearmaps.proj2ab.WeirdPointSet;

import java.util.*;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 *
 * @author Alan Yao, Josh Hug, ________
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {
    public List<Node> nodes;
    MyTrieSet trie;
    Map<String, List<String>> mprefix = new HashMap<>();
    Map<String, List<Map<String, Object>>> mloc = new HashMap<>();

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        // You might find it helpful to uncomment the line below:
        List<Node> nodes = this.getNodes();
        this.nodes = nodes;

        trie = new MyTrieSet();

        for (int i = 0; i < nodes.size(); i++) {
            Node n = nodes.get(i);
            String name = n.name();

            if (name != null) {
                String clean = cleanString(name);
                trie.add(clean); // trie contains cleanstrings

                if (mprefix.containsKey(clean)) {
                    List<String> lista = mprefix.get(clean);
                    if (!lista.contains(name)) {
                        lista.add(name);
                    }

                } else {
                    ArrayList a = new ArrayList();
                    a.add(name);
                    mprefix.put(clean, a);
                }

                HashMap<String, Object> ma = new HashMap();
                ma.put("lat", n.lat());
                ma.put("lon", n.lon());
                ma.put("name", n.name());
                ma.put("id", n.id());

                if (!mloc.containsKey(clean)) {
                    List al = new ArrayList<Map<String, Object>>();
                    al.add(ma);
                    mloc.put(clean,  al);
                } else {

                    mloc.get(clean).add(ma);
                }

            }
        }

    }


    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        Map<Point, Node> m = new HashMap();
        for (int i = 0; i < nodes.size(); i++) {
            Node n = nodes.get(i);
            if (!neighbors(n.id()).isEmpty()) {
                Point a = new Point(n.lon(), n.lat());
                m.put(a, nodes.get(i));
            }
        }
        ArrayList points = new ArrayList(m.keySet());

        WeirdPointSet wps = new WeirdPointSet(points);
        Point near = wps.nearest(lon, lat);
        Node n = m.get(near);
        return n.id();
    }


    /**
     * For Project Part III (gold points)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        String pre = cleanString(prefix);
        List<String> T = trie.keysWithPrefix(pre);
        List<String> result = new ArrayList<>();
        for (String t: T) {
            List<String> R = mprefix.get(t);
            for (String r: R) {
                result.add(r);
            }

        }
        return result;
    }

    /**
     * For Project Part III (gold points)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        String locationCleaned = cleanString(locationName);
        return mloc.get(locationCleaned);
    }


    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

}

