# !/usr/bin/env python3

import sys
import requests
from requests.auth import HTTPBasicAuth
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
auth = None
api_token = 'yazbSE0WS5oIvF4a0QUG6490'

jira_base_url = 'https://toranotec.atlassian.net/'
jira_project_key = 'TOR2D'
search_key = 'TestCaseId'
automated_transition_id = '10153'
automated_status_id = '10154'


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
        filename = '../target/cucumber.json'
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


def jira_header():
    global jira_headers
    jira_headers = {
        'Content-Type': 'application/json;charset=UTF-8',
        # 'Authorization': f'Basic{str(auth_string, "utf-8")}',
        'Accept': 'application/json'
    }


def jira_auth(username, password):
    global auth
    auth = HTTPBasicAuth('vvardhan@toranotec-partner.com', 'yazbSE0WS5oIvF4a0QUG6490')


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
        tag_name = [x['name'] for x in scenario['tags'] if (search_key in x['name'] and jira_project_key in x['name'])][
            0]
        jira_id = tag_name[tag_name.find(jira_project_key + '-'):]
        tickets_list.append(jira_id.strip())
        # Remove duplicates
        tickets_list = list(set(tickets_list))
    return tickets_list


def update_ticket_status(tickets):
    issues_updated_count = 0
    automated_tickets = list()
    not_updated_tickets = list()
    already_automated_tickets = list()
    # First get the issue details to see if it's already Automated
    for ticket in tickets:
        get_endpoint = f'rest/api/2/issue/{ticket}'
        response = requests.get(jira_base_url + get_endpoint, headers=jira_headers, auth=auth)
        if response.status_code != 200:
            print("ERROR: Error returned when trying to get the status of ticket: " + ticket + " " + str(
                response.status_code))
            if response.text:
                print("ERROR: " + response.text)
            continue
        else:
            json_response = json.loads(response.text)
            current_status = json_response['fields']['status']['id']
            if current_status == automated_status_id:
                print(f'INFO: {ticket} is already AUTOMATED')
                already_automated_tickets.append(ticket)
                continue
            elif current_status == automated_transition_id:
                print(f'INFO: {ticket} is not AUTOMATED, updating status...')
                transition_response = update_status(ticket)
                print('Updated status to resolve', response.status_code)
                if transition_response.status_code != 204:
                    print("ERROR: Error returned when trying to update the status of ticket: " + ticket + " " + str(
                        transition_response.status_code))
                    not_updated_tickets.append(ticket)
                    if transition_response.text:
                        print("ERROR: " + response.text)
                else:
                    print(f'INFO: Set {ticket} to AUTOMATED')
                    automated_tickets.append(ticket)
                    issues_updated_count += 1
            else:
                print(f'WARNING: {ticket} is not set To Be Automated status earlier...')
                print(f'Please make sure to set To Be Automated status and then run again')
    if issues_updated_count > 0:
        print('\n' + str(issues_updated_count) + " issue(s) set to AUTOMATED")
        print('\n**********SUMMARY**********')
        print('\n AUTOMATED : ', automated_tickets)
        print('\n NO CHANGE : ', not_updated_tickets)
    else:
        print('\nAll issues were already set to AUTOMATED')
        print('\n**********SUMMARY**********')
        print('\n Already Automated :', already_automated_tickets)


def update_status(ticket):
    url_transitions = f'rest/api/2/issue/{ticket}/transitions'
    payload = json.dumps({
        "transition": {
            "id": "191",
            "name": "Automated",
            "hasScreen": False,
            "isGlobal": False,
            "isInitial": False,
            "isConditional": False,
            "isLooped": False,
            "to": {
                "id": "10154",
                "name": "Automated",
                "statusCategory": {
                    "id": 4
                }
            }
        }
    }
    )
    response = requests.request("POST", jira_base_url + url_transitions, data=payload, headers=jira_headers, auth=auth)
    return response


if __name__ == '__main__':
    jira_auth('vvardhan@toranotec-partner.com', 'yazbSE0WS5oIvF4a0QUG6490')
    jira_header()
    parse_inputs()
    jira_tickets = read_in_zephyr_tests(filename)
    update_ticket_status(jira_tickets)
    exit()
