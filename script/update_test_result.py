import sys
import requests
import json


"""
This python3 script parses the test output file for the associated Zephyr Test Case IDs and updates them to
'Pass' or 'Fail' if they are in any other state
"""
jira_username = ''
jira_password = ''
jira_headers = {}
filename = ''
api_token = 'yazbSE0WS5oIvF4a0QUG6490'

jira_base_url = 'https://toranotec.atlassian.net/'
jira_project_key = 'TOR2D'
search_key = 'TestCaseId'
automated_transition_id = '81'
automated_status_id = '12829'

test_folders = {
    'Home': {
        'url': 'https://prod-play.zephyr4jiracloud.com/connect/public/rest/api/2.0/executions/search/folder/1ecf9b6f-dab4-4465-a9e2-547d0f5955b8?projectId=10124&versionId=-1&cycleId=60566fa1-a88a-4c4a-bd44-811971e905cd&offset=0&action=expand&sortBy=&sortOrder=asc&size=50&_=1653879510827'
    },
    'Invest': {
        'url': 'https://prod-play.zephyr4jiracloud.com/connect/public/rest/api/2.0/executions/search/folder/5466339f-ead4-49df-9c9f-5751b898d449?projectId=10124&versionId=-1&cycleId=60566fa1-a88a-4c4a-bd44-811971e905cd&offset=0&action=expand&sortBy=&sortOrder=asc&size=50&_=1653879421722'
    },
    'Menu': {
        'url': 'https://prod-play.zephyr4jiracloud.com/connect/public/rest/api/2.0/executions/search/folder/a223f914-a938-4501-983c-5ce0a40c2fba?projectId=10124&versionId=-1&cycleId=60566fa1-a88a-4c4a-bd44-811971e905cd&offset=0&action=expand&sortBy=&sortOrder=asc&size=50&_=1653879543410'
    },
    'Points': {
        'url': 'https://prod-play.zephyr4jiracloud.com/connect/public/rest/api/2.0/executions/search/folder/3908fa84-65d3-4d2d-9973-da4c56cd596c?projectId=10124&versionId=-1&cycleId=60566fa1-a88a-4c4a-bd44-811971e905cd&offset=0&action=expand&sortBy=&sortOrder=asc&size=50&_=1653879585186'
    }
}

project_summary = {
    'cycleId' : '60566fa1-a88a-4c4a-bd44-811971e905cd',
    'folderIds': {
        'Home': '1ecf9b6f-dab4-4465-a9e2-547d0f5955b8',
        'Invest': '5466339f-ead4-49df-9c9f-5751b898d449',
        'Menu': 'a223f914-a938-4501-983c-5ce0a40c2fba',
        'Points': '3908fa84-65d3-4d2d-9973-da4c56cd596c'
    },
    'projectId': 10124
}
jira_headers = {
    'Content-Type': 'application/json;charset=UTF-8',
    'Accept': 'application/json',
    'x-acpt': '7e07efa3325174a5c68ef18f5b03cbce1151342f043db5bead534c686243cc1692585d2ecea48f6a5e8d131f1a701e447017d6e969062b4f695caf77e1b2ed81290e891c09906ac4c55e94a795e0733e641d8303030470b314b5d3226833250e5dda64756a3d6f7bc475089be14538b5cf67c30e1ee9994d182d86acd8341ae41051b63f80a7bfa310af5b8290e5bab442f08a03a557dc46060c853b5bf7db14a8e79241ba57051d1c344633eeb222e2052f938a0e532f956e54e3abdb9aa85f'
}


def read_in_zephyr_tests(filename):
    rd = open(filename, 'r')
    data = json.load(rd)
    rd.close()
    scenarios = data[0]
    tickets_list = []
    for scenario in scenarios['elements']:
        d = {}
        tag_name = [x['name'] for x in scenario['tags'] if (search_key in x['name'] and jira_project_key in x['name'])][0]
        d['status'] = True
        status = [y for x in scenario['steps'] for y in x]
        if 'failed' in status or 'skipped':
            d['status'] = False
        jira_id = tag_name[tag_name.find(jira_project_key + '-'):]
        d['ticket'] = jira_id.strip()
        tickets_list.append(d)
    return tickets_list


def search_test_cases_list(tickets):
    test_cases = []
    execution_url ='https://prod-play.zephyr4jiracloud.com/connect/public/rest/api/1.0/execution/'
    for key, value in test_folders.items():
        response = requests.get(value['url'], headers=jira_headers)
        if response.status_code == 200:
            for test_case in json.loads(response.text)['searchResult']['searchObjectList']:
                url = execution_url + test_case['execution']['id']+'?projectId=10124&issueId='+str(test_case['execution']['issueId'])+'&_=1653887793235'
                data_pass = json.dumps({"cycleId": test_case['execution']['cycleId'],"folderId":test_case['execution']['folderId'],"id":test_case['execution']['id'],"issueId":test_case['execution']['issueId'],"projectId":10124,"status":{"id":1},"versionId":-1})
                data_fail = json.dumps({"cycleId": test_case['execution']['cycleId'],"folderId":test_case['execution']['folderId'],"id":test_case['execution']['id'],"issueId":test_case['execution']['issueId'],"projectId":10124,"status":{"id":2},"versionId":-1})
                data_un_executed = json.dumps({"cycleId": test_case['execution']['cycleId'],"folderId":test_case['execution']['folderId'],"id":test_case['execution']['id'],"issueId":test_case['execution']['issueId'],"projectId":10124,"status":{"id":-1},"versionId":-1})
                for ticket in tickets:
                    d = {}
                    if ticket['ticket'] == test_case['issueKey']:
                        if ticket['status']:
                            response = requests.put(url, data=data_pass, headers=jira_headers)
                            d['ticket'] = json.loads(response.text)['issueKey']
                            d['status'] = 'Pass'
                        else:
                            response = requests.put(url, data=data_fail, headers=jira_headers)
                            d['ticket'] = json.loads(response.text)['issueKey']
                            d['status'] = 'Fail'
                        test_cases.append(d)
        else:
            print('Status code :: ', response.status_code)
            print('Message :: ', response.text)
    print('Summary - ', test_cases)


if __name__ == '__main__':
    filepath = r"D:\Toranoko Automation\AutomationFramework-Mobile-master\AutomationFramework-Mobile-master\target\cucumber.json"
    jira_tickets = read_in_zephyr_tests(filepath)
    search_test_cases_list(jira_tickets)
