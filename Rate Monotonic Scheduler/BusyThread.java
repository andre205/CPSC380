import java.util.Random;
import java.util.concurrent.Semaphore;

public class BusyThread {

    //affects difficulty of dowork function
    public static final int DOWORK_REPS = 1000000;

    public int runCount;
    public boolean done = false;
    public int[][] doWorkMatrix = new int[10][10];
    public Semaphore sem = new Semaphore(0);
    public int completions;
    public int priority;
    private int[] colList = {1,3,5,7,9,0,2,4,6,8};
    private boolean schedForcedQuit = false;

    Thread thread;

    public BusyThread(int rc, int p)
    {
        this.completions = 0;
        this.runCount = rc;
        //thread.setPriority(Thread.MAX_PRIORITY - p);
        this.priority = Thread.MAX_PRIORITY - p;
        //populate do work matrix
        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < 10; ++j) {
                doWorkMatrix[i][j] = 1;
            }
        }

        this.schedForcedQuit = false;

        //System.out.println("Created bt");
    }

    public boolean isDone()
    {
        return this.done;
    }


    public void doWork()
    {
        int temp = 0;
        //TOTAL REPITITIONS (increase difficulty)
        for(int rep = 0; rep < DOWORK_REPS; ++rep)
        {
            for(int k = 0; k < 10; ++k)
            {
                temp = colList[k];
                for(int j = 0; j < 10; ++j)
                {
                    doWorkMatrix[j][temp] = doWorkMatrix[k][j];
                }
            }
        }
    }

    public void createThread()
    {
      this.thread = new Thread(new Runnable() {
          @Override
          public void run()
          {
              try
              {
                  while (!Thread.currentThread().isInterrupted())
                  {
                      while(!schedForcedQuit)
                      {
                          try
                          {
                              sem.acquire();

                              done = false;

                              for(int i = 0; i < runCount; i++)
                              {
                                  doWork();
                                  completions++;
                                  //System.out.println("Completions " + completions);
                              }

                              done = true;
                          }
                          catch (Exception e)
                          {
                              //e.printStackTrace();
                          }
                      }
                      if(schedForcedQuit)
                      {
                          //System.out.println("QUITTING THREAD");
                          break;
                      }
                  }
              }
              catch (Exception e)
              {
                    //Thread.currentThread().interrupt();
                    System.out.println("HARD INTERRUPTED");
             }

          }
      });

        this.thread.setPriority(this.priority);
    }

    public void schedForceQuit()
    {
        this.schedForcedQuit = true;
    }

}
