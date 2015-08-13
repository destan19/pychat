
#!/Library/Frameworks/Python.framework/Versions/2.5/bin/python
# encoding: utf-8
import sys, time
import thread
 
SLEEP_TIME = 0.0001
 
def run_benchmark(n, m):
    # print(">> Python 2.5.1, stackless 3.1b3 here (N=%d, M=%d)!\n" % (n, m))
    locks = [thread.allocate_lock() for i in xrange(n)]
    firstP = cin = []
    cin_lock_id = 0
    for s in xrange(1, n):
        seqn = s
        cout = []
        cout_lock_id = s
        # print("*> s = %d" % (seqn, ))
        thread.start_new_thread(loop, (seqn, locks, cin, cin_lock_id, cout, cout_lock_id))
        cin = cout
        cin_lock_id = cout_lock_id
    else:
        seqn = s+1
        # print("$> s = %d" % (seqn, ))
        thread.start_new_thread(mloop, (seqn, locks, cin, cin_lock_id))
    for r in xrange(m-1, -1, -1):
        # print("+ sending Msg#  %d" % r)
        lock = locks[0]
        lock.acquire()
        firstP.append(r)
        lock.release()
        time.sleep(SLEEP_TIME)
    try:
        while True:
            time.sleep(SLEEP_TIME)
    except:
        pass
def loop(s, locks, cin, cin_lock_id, cout, cout_lock_id):
    while True:
        lock = locks[cin_lock_id]
        lock.acquire()
        if len(cin) > 0:
            r = cin.pop(0)
            lock.release()
        else:
            lock.release()
            time.sleep(SLEEP_TIME)
            continue
        lock = locks[cout_lock_id]
        lock.acquire()
        cout.append(r)
        lock.release()
        if r > 0: 
            # print(": Proc: <%s>, Seq#: %s, Msg#: %s .." % (pid(), s, r))
            pass
        else:
            # print("* Proc: <%s>, Seq#: %s, Msg#: terminate!" % (pid(), s))
            break
def mloop(s, locks, cin, cin_lock_id):
    while True:
        lock = locks[cin_lock_id]
        lock.acquire()
        if len(cin) > 0:
            r = cin.pop(0)
            lock.release()
        else:
            lock.release()
            time.sleep(SLEEP_TIME)
            continue
        if r > 0: 
            # print("> Proc: <%s>, Seq#: %s, Msg#: %s .." % (pid(), s, r))
            pass
        else:
            # print("@ Proc: <%s>, Seq#: %s, ring terminated." % (pid(), s))
            break
    thread.interrupt_main()
 
def pid(): return thread.get_ident()
 
if __name__ == '__main__':
    run_benchmark(int(sys.argv[1]), int(sys.argv[2]))