public class Project3
{
    public static void main(String[] args)
    {
        System.out.println("DOWORK REPITITIONS: " + BusyThread.DOWORK_REPS);

        Scheduler sched = new Scheduler();
        sched.schedThread.start();
        sched.joinAll();

        try
        {
            sched.schedThread.join();
        }
        catch(Exception e)
        {
            System.out.println("SCHED THREAD JOIN FAIL");
            e.printStackTrace();
        }

        sched.printResults();

    }
}
