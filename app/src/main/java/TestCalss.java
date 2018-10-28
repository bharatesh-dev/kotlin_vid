import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.operators.single.SingleToObservable;

public class TestCalss {


    void test() {
        Observable<String> os = Observable.just("", "", "");

        //Single<String> si = Single.just("Sdf");
        Single<String> si = Single.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "SDF";
            }
        });

        si.subscribe(new SingleObserver<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(String s) {

            }

            @Override
            public void onError(Throwable e) {

            }
        });

    }
}
