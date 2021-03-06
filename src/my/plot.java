package my;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;
import jason.asSyntax.StringTerm;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;

/** Plot a graph with the score of the players */
public class plot extends DefaultInternalAction {

    Map<Integer,Integer> values = new HashMap<Integer,Integer>();

    static DefaultXYDataset dataset = new DefaultXYDataset();
    static {
        JFreeChart xyc = ChartFactory.createXYLineChart(
                             "Network efficiency",
                             "Turn",
                             "Efficiency",
                             dataset, // dataset,
                             PlotOrientation.VERTICAL, // orientation,
                             true, // legend,
                             true, // tooltips,
                             true); // urls

        JFrame frame = new ChartFrame("Electricity Market Simulation", xyc);
        frame.setSize(800,500);
        frame.setVisible(true);
    }

    @Override
    public Object execute(final TransitionSystem ts, final Unifier un, final Term[] args) throws Exception {
        String series  = (String) ((StringTerm)args[0]).getString();
        int step = (int)((NumberTerm)args[1]).solve();
		int score = (int)((NumberTerm)args[2]).solve();
        addValue(series, step, score);
        return true;
    }

    void addValue(String series, int step, int vl) {
        values.put(step,vl);
        double[][] data = getData(step);
        synchronized (dataset) {
            dataset.addSeries(series, data);
        }
    }

    private double[][] getData(int maxStep) {
        double[][] r = new double[2][maxStep+1];
        int vl = 0;
        for (int step = 0; step<=maxStep; step++) {
            if (values.containsKey(step))
                vl = values.get(step);
            r[0][step] = step;
            r[1][step] = vl;
        }
        return r;
    }
}
