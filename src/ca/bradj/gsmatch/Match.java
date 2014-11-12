package ca.bradj.gsmatch;

import com.google.common.base.Preconditions;

import ca.bradj.common.base.Preconditions2;

public class Match {

    private String name;
    private double percentConfidence;

    public Match(String trim, double percent) {
        this.name = Preconditions2.checkNotEmpty(trim);
        this.percentConfidence = Preconditions.checkNotNull(percent);
    }

    public static Match withPercent(String trim, double percent) {
        return new Match(trim, percent);
    }

    public String getName() {
        return name;
    }

    public double getPercentConfidence() {
        return percentConfidence;
    }

    @Override
    public String toString() {
        return "Match [name=" + name + ", percentConfidence=" + percentConfidence + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        long temp;
        temp = Double.doubleToLongBits(percentConfidence);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Match other = (Match) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (Double.doubleToLongBits(percentConfidence) != Double.doubleToLongBits(other.percentConfidence))
            return false;
        return true;
    }

}
