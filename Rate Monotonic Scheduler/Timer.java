import java.util.concurrent.Semaphore;

public class Timer
{
    //public Double releaseCount = 0.0;

    public Timer(){};

    public double getTime()
    {
        return (System.nanoTime() / 1000000);
    }

    public void run(Semaphore s)
    {
        double startTime = System.nanoTime();
        //System.out.println("START TIME: " + startTime);

        while((System.nanoTime() / 1000000) < (startTime/1000000 + 10*16*100))
        {
              //%10 = 10ms time interval
              if ( ((System.nanoTime() - startTime)/1000000)  % 100 == 0)
              {
                  s.release();
                  //releaseCount++;
                  //System.out.println("RELEASING SCHED SEM");
                  // System.out.println("RCOUNT " + releaseCount );
                  // System.out.println("T ELAPSED " + (System.nanoTime() - startTime)/1000000);
              }
        }

        //100ms time interval, 10 iterations of 16 cycles
        // for(int i = 0; i < 160; ++i)
        // {
        //     try
        //     {
        //         s.release();
        //         Thread.sleep(10);
        //     }
        //     catch (Exception e)
        //     {
        //         e.printStackTrace();
        //     }
        // }
    }

}
