package fe.linksheet.interconnect;

import android.content.ComponentName;
import fe.linksheet.interconnect.IDomainSelectionResultCallback;
import fe.linksheet.interconnect.ISelectedDomainsCallback;
import fe.linksheet.interconnect.StringParceledListSlice;

interface ILinkSheetService {
    void getSelectedDomainsAsync(String packageName, ISelectedDomainsCallback callback);
    void selectDomains(String packageName, in StringParceledListSlice domains, in ComponentName componentName);
    void selectDomainsWithCallback(String packageName, in StringParceledListSlice domains, in ComponentName componentName, IDomainSelectionResultCallback callback);
}
