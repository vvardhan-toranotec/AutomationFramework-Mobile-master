# !/usr/bin/env python3

import sys
import requests
from base64 import standard_b64encode
import json

"""
This python3 script parses the test output file for the associated Zephyr Test Case IDs and sets them to
'Automated' if they are in any other state
It takes three arguments:
- Jira Username - username for the bot account to query jira tickets
- Jira Password - password for the bot account to query jira tickets
- Filename - file that contains the test data
"""
jira_username = ''
jira_password = ''
jira_headers = {}
filename = ''

jira_base_url = 'https://toranotec.atlassian.net/'
jira_project_key = 'TOR2D'
search_key = 'TestCaseId'
automated_transition_id = '81'
automated_status_id = '12829'


def parse_inputs():
    global jira_username
    global jira_password
    global filename

    try:
        # jira_username = sys.argv[1]
        # jira_password = sys.argv[2]
        # filename = sys.argv[3]
        jira_username = 'vvardhan@toranotec-partner.com'
        jira_password = 'Vishnu@2021'
        filename = r"D:\Toranoko Automation\AutomationFramework-Mobile-master\AutomationFramework-Mobile-master\target\cucumber.json"
    except IndexError:
        print("ERROR: username, password, and filename command line arguments are required for this script")
        exit(1)
    if len(jira_username) < 1:
        print("ERROR: username command line argument is required for this script")
        exit(1)
    elif len(jira_password) < 1:
        print("ERROR: password command line argument is required for this script")
        exit(1)
    elif len(filename) < 1:
        print("ERROR: filename command line argument is required for this script")
        exit(1)


def jira_auth_header(username, password):
    global jira_headers

    auth_string: bytes = standard_b64encode(bytes(f'{username}:{password}', 'utf-8'))

    jira_headers = {
        'Content-Type': 'application/json;charset=UTF-8',
        'Authorization': f'Basic{str(auth_string, "utf-8")}',
        'Accept': 'application/json'
    }


def read_in_zephyr_tests(filename):
    print(filename)
    rd = open(filename, 'r')
    data = json.load(rd)
    rd.close()
    print(data)
    print(len(data))
    scenarios = data[0]
    tickets_list = []
    for scenario in scenarios['elements']:
        tag_name = [x['name'] for x in scenario['tags'] if (search_key in x['name'] and jira_project_key in x['name'])][0]
        jira_id = tag_name[tag_name.find(jira_project_key + '-'):]
        tickets_list.append(jira_id.strip())
        # Remove duplicates
        tickets_list = list(set(tickets_list))
    return tickets_list


def update_ticket_status(tickets):
    issues_updated_count = 0
    # First get the issue details to see if it's already Automated
    for ticket in tickets:
        get_endpoint = f'rest/api/2/issue/{ticket}'
        transition_endpoint = f'rest/api/2/issues/{ticket}/transitions'

        print(jira_base_url + get_endpoint)
        response = requests.get(jira_base_url + get_endpoint, headers=jira_headers)
        print(response.text)
        if response.status_code != 200:
            print("ERROR: Error returned when trying to get the status of ticket: " + ticket + " " +str(response.status_code))
            if response.text:
                print("ERROR: " + response.text)
            continue
        else:
            json_response = json.loads(response.text)
            current_status = json_response['fields']['status']['id']

            if current_status == automated_status_id:
                print(f'INFO: {ticket} is already AUTOMATED')
                continue
            else:
                print(f'INFO: {ticket} is not AUTOMATED, updating status...')

        body = {
            'transition': {
                "id": automated_transition_id
            }
        }
        # response = requests.post(jira_base_url + transition_endpoint, data=None, json=body, headers=jira_headers)
        # if response.status_code != 204:
        #     print("ERROR: Error returned when trying to update the status of ticket: " + ticket + " " +str(response.status_code))
        #     if response.text:
        #         print("ERROR: " + response.text)
        # else:
        #     print(f'INFO: Set {ticket} to AUTOMATED')
        #     issues_updated_count += 1
    if issues_updated_count > 0:
        print('\n' + str(issues_updated_count) + " issue(s) set to AUTOMATED")
    else:
        print('\nAll issues were already set to AUTOMATED')


if __name__ == '__main__':
    parse_inputs()
    jira_auth_header(jira_username, jira_password)
    jira_tickets = read_in_zephyr_tests(filename)
    print(jira_tickets)
    update_ticket_status(jira_tickets)
    # exit()
