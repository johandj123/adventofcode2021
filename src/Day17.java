public class Day17 {
    public static void main(String[] args) {
        int count = 0;
        int ymax = 0;
        for (int vx = 1; vx <= 70; vx++) {
            for (int vy = -189; vy <= 2000; vy++) {
                Integer y = highest(vx, vy);
                if (y != null) {
                    ymax = Integer.max(y, ymax);
                    count++;
                }
            }
        }
        System.out.println("First: " + ymax);
        System.out.println("Second: " + count);
    }

    public static Integer highest(int vx,int vy)
    {
        Probe probe = new Probe(vx, vy);
        int ymax = 0;
        while (!probe.inTarget() && !probe.outOfBounds()) {
            probe.step();
            ymax = Math.max(ymax, probe.y);
        }
        return probe.inTarget() ? ymax : null;
    }

    static class Probe
    {
        int x = 0;
        int y = 0;
        int vx;
        int vy;

        public Probe(int vx, int vy) {
            this.vx = vx;
            this.vy = vy;
        }

        public void step()
        {
            x += vx;
            y += vy;
            if (vx > 0) {
                vx--;
            } else if (vx < 0) {
                vx++;
            }
            vy--;
        }

        public boolean inTarget()
        {
            return x >= 48 && x <= 70 && y >= -189 && y <= -148;
        }

        public boolean outOfBounds()
        {
            return vy < 0 && y <= -189;
        }
    }
}
