import {Component , OnDestroy} from '@angular/core';
import {Subscription} from "rxjs";
import {NavigationStart , Router} from "@angular/router";

export let browserRefresh = false;

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnDestroy {
  title = 'Breaking Chess';
  subscription: Subscription;

  constructor(private router: Router) {
    this.subscription = router.events.subscribe((event) => {
      if (event instanceof NavigationStart) {
        browserRefresh = !router.navigated;
      }
    });
  }


  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
}
