import requests, json
import logging
import sys
from pprint import pprint
#setup logging
logging.basicConfig(level=logging.INFO,
                format='%(asctime)s %(levelname)s %(message)s',
                filename='Restapi_validation.log',
                datefmt='%a, %d %b %Y %H:%M:%S',
                filemode='w')
service_status = ""
url =  "http://a101d6968c33c11e9802b0e65d0987dc-468803531.us-east-1.elb.amazonaws.com:8090/iindex.html"
headers = {'Content-Type': 'application/json', 'Accept':'application/json'}
logging.info('service url: %s' %(url))
try:
        r = requests.get(url, headers=headers, verify=False)
        logging.info("service response %s" % (r.status_code))
        if str(r.status_code) != '200' or r.status_code == '':
                pprint('failed response %s' %r.status_code)
                service_status = "failed"
        elif str(r.status_code) == '200':
                pprint('success response %s' %r.status_code)
                service_status = "success"
        else:
                service_status = "failed"
                logging.error("error" )
                sys.exit(1)
except Exception as e:

    logging.error(e)
    pprint(e)
    service_status = "failed"
    print(service_status)
    exit(1)
if service_status == "success":
        pprint("retuning success status")
        sys.exit(0)
else:
        sys.exit(1)
