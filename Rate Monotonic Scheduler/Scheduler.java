import java.util.concurrent.Semaphore;

public class Scheduler
{
    public int overrunCount_Thread0;
    public int overrunCount_Thread1;
    public int overrunCount_Thread2;
    public int overrunCount_Thread3;

    public int timeValue;

    BusyThread bt0;
    BusyThread bt1;
    BusyThread bt2;
    BusyThread bt3;

    Thread schedThread;

    public Semaphore sem = new Semaphore(0);

    //on scheduler creation, create and start 4 busythreads with decreasing priority
    //as well as a scheduler thread that runs schedule()
    public Scheduler()
    {
        bt0 = new BusyThread(1, 2);
        bt1 = new BusyThread(2, 3);
        bt2 = new BusyThread(4, 4);
        bt3 = new BusyThread(16, 5);
        timeValue = 0;

        bt0.createThread();
        bt1.createThread();
        bt2.createThread();
        bt3.createThread();

        bt0.thread.start();
        bt1.thread.start();
        bt2.thread.start();
        bt3.thread.start();

        this.schedThread = new Thread(new Runnable()
        {
              @Override
              public void run()
              {
                     schedule();
                     killThreads();
              }
        });

        this.schedThread.setPriority(Thread.MAX_PRIORITY);

    }

    public void joinAll()
    {
        try
        {
            bt0.thread.join();
            bt1.thread.join();
            bt2.thread.join();
            bt3.thread.join();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Failed to join threads.");
        }

    }

    //time 0 release all semaphores
    //times 1-16, check completion of each thread and record overruns
    //and rerelease semaphores at appropriate intervals
    public void schedule()
    {
        //Time 0 release all semaphores
        bt0.sem.release();
        bt1.sem.release();
        bt2.sem.release();
        bt3.sem.release();

        for(int i = 0; i < 160; ++i)
        {
            try
            {
                Thread.sleep(10);
                //sem.acquire();

            }
            catch(Exception e){System.out.println("Sched failed to acquire sem");}

            timeValue++;
            //System.out.println("TIME " + timeValue);
            // System.out.println("OVERRUNS:    " + overrunCount_Thread0 + " " + overrunCount_Thread1 + " " + overrunCount_Thread2 + " " + overrunCount_Thread3);
            // System.out.println("COMPLETIONS  " + bt0.completions + " " + bt1.completions + " " + bt2.completions + " " + bt3.completions);

            //check if previous bt0 iteration failed to complete
            if (!bt0.isDone())
            {
                //System.out.println("Overrun bt0");
                overrunCount_Thread0++;
            }
            //release bt0 sem and do work
            if(timeValue > 0) //don't double release bt0 sem at t0
            {
                bt0.sem.release();
            }

            if (timeValue % 2 == 0)
            {
                if (!bt1.isDone())
                {
                    //System.out.println("Overrun bt1");
                    overrunCount_Thread1++;
                }


                bt1.sem.release();

            }

            if (timeValue % 4 == 0)
            {
                if (!bt2.isDone())
                {
                    //System.out.println("Overrun bt2");
                    overrunCount_Thread2++;
                }

                bt2.sem.release();

            }

            if (timeValue % 16 == 0)
            {
                if (!bt3.isDone())
                {
                    //System.out.println("Overrun bt3");
                    overrunCount_Thread3++;
                }

                bt3.sem.release();

            }

        }
    }

    public void killThreads()
    {
        //tell the threads to stop making progress (time is up)
        bt0.schedForceQuit();
        bt1.schedForceQuit();
        bt2.schedForceQuit();
        bt3.schedForceQuit();

        //force interrupt threads because they act up if you don't for some reason
        bt0.thread.interrupt();
        bt1.thread.interrupt();
        bt2.thread.interrupt();
        bt3.thread.interrupt();

    }

    public void printResults()
    {
        System.out.println("RESULTS");
        System.out.println("bt0 completions:    " + bt0.completions);
        System.out.println("bt0 overruns:       " + overrunCount_Thread0);
        System.out.println();
        System.out.println("bt1 completions:    " + bt1.completions);
        System.out.println("bt1 overruns:       " + overrunCount_Thread1);
        System.out.println();
        System.out.println("bt2 completions:    " + bt2.completions);
        System.out.println("bt2 overruns:       " + overrunCount_Thread2);
        System.out.println();
        System.out.println("bt3 completions:    " + bt3.completions);
        System.out.println("bt3 overruns:       " + overrunCount_Thread3);

    }

}
